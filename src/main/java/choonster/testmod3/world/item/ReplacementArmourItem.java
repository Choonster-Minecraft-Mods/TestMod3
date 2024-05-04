package choonster.testmod3.world.item;

import choonster.testmod3.init.ModDataComponents;
import choonster.testmod3.serialization.VanillaCodecs;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.InventoryUtils;
import choonster.testmod3.util.InventoryUtils.EntityInventoryType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An armour item that replaces your other armour when equipped and restores it when unequipped.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2595100-persistent-variables-for-armor
 *
 * @author Choonster
 */
public class ReplacementArmourItem extends ArmorItem {
	private static final Logger LOGGER = LogUtils.getLogger();

	/**
	 * The items to replace the other armour with.
	 */
	private final Set<Supplier<ItemStack>> replacementItems;

	public ReplacementArmourItem(final Holder<ArmorMaterial> material, final ArmorItem.Type type, final Properties properties, final Collection<Supplier<ItemStack>> replacementItems) {
		super(material, type, properties);

		this.replacementItems = ImmutableSet.copyOf(
				replacementItems
						.stream()
						.map(Lazy::of)
						.collect(Collectors.toSet())
		);
	}

	/**
	 * Has this item replaced the other armour?
	 *
	 * @param stack The ItemStack of this item
	 * @return Has this item replaced the other armour?
	 */
	public static boolean hasReplacedArmour(final ItemStack stack) {
		return stack.has(ModDataComponents.REPLACED_ARMOUR.get());
	}

	/**
	 * Save the entity's armour and replace it with the replacements defined for this item.
	 *
	 * @param stack  The ItemStack of this item
	 * @param entity The entity
	 */
	private void replaceArmour(final ItemStack stack, final LivingEntity entity) {
		final var replacedArmour = ImmutableList.<ReplacedArmour.Entry>builder();

		// Create a mutable copy of the replacements
		final var replacements = replacementItems
				.stream()
				.map(Supplier::get)
				.collect(Collectors.toSet());

		Stream.of(ArmorItem.Type.values()) // For each armour type,
				.filter(type -> type != getType()) // If it's not this item's armour type,
				.forEach(type -> {
					final var equipmentSlot = type.getSlot();
					final var optionalReplacement = replacements.stream()
							.filter(replacementStack -> replacementStack.getItem().canEquip(replacementStack, equipmentSlot, entity))
							.findFirst();

					optionalReplacement.ifPresent(replacement -> { // If there's a replacement for this armour type,
						replacements.remove(replacement); // Don't use it for any other armour type

						final var original = entity.getItemBySlot(equipmentSlot);

						// Create an entry with the slot and the original item
						final var entry = new ReplacedArmour.Entry(equipmentSlot, original);

						// Add it to the list of replaced armour
						replacedArmour.add(entry);

						entity.setItemSlot(equipmentSlot, replacement.copy()); // Equip a copy of the replacement
						LOGGER.info("Equipped replacement {} to {}, replacing {}", replacement, type, original);
					});
				});

		// Save the replaced armour to the ItemStack
		final var newReplacedArmor = new ReplacedArmour(replacedArmour.build());
		stack.set(ModDataComponents.REPLACED_ARMOUR.get(), newReplacedArmor);
	}

	/**
	 * Restore the entity's saved armour from this item's ItemStack NBT.
	 *
	 * @param stack  The ItemStack of this item
	 * @param entity The entity
	 */
	private void restoreArmour(final ItemStack stack, final LivingEntity entity) {
		final var replacedArmour = stack.getOrDefault(ModDataComponents.REPLACED_ARMOUR.get(), new ReplacedArmour(ImmutableList.of()));

		// For each saved armour item,
		for (final var entry : replacedArmour.replacedArmour) {
			// Get the original item and slot
			final var original = entry.replacedArmour;
			final var equipmentSlot = entry.slot;

			final var current = entity.getItemBySlot(equipmentSlot);

			// Is the item currently in the slot one of the replacements defined for this item?
			final var isReplacement = replacementItems
					.stream()
					.map(Supplier::get)
					.anyMatch(replacement -> ItemStack.matches(replacement, current));

			if (original.isEmpty()) { // If the original item is empty,
				if (isReplacement) { // If the current item is a replacement,
					LOGGER.info("Original item for {} is empty, clearing replacement", equipmentSlot);
					entity.setItemSlot(equipmentSlot, ItemStack.EMPTY); // Delete it
				} else { // Else do nothing
					LOGGER.info("Original item for {} is empty, leaving current item", equipmentSlot);
				}
			} else {
				LOGGER.info("Restoring original {} to {}, replacing {}", original, equipmentSlot, current);

				// If the current item isn't a replacement and the entity is a player, try to add it to their inventory or drop it on the ground
				if (!isReplacement && entity instanceof Player) {
					ItemHandlerHelper.giveItemToPlayer((Player) entity, current);
				}

				entity.setItemSlot(equipmentSlot, original); // Equip the original item
			}
		}

		stack.remove(ModDataComponents.REPLACED_ARMOUR.get());
	}

	/**
	 * Restore the entity's saved armour if the ItemStack is in the specified inventory slot.
	 *
	 * @param inventory The inventory
	 * @param slot      The inventory slot
	 * @param stack     The ItemStack of this item
	 * @param entity    The entity
	 * @return Was the armour restored?
	 */
	private boolean tryRestoreArmour(final IItemHandler inventory, final int slot, final ItemStack stack, final LivingEntity entity) {
		if (slot < inventory.getSlots() && inventory.getStackInSlot(slot) == stack) {
			restoreArmour(stack, entity); // Restore the entity's armour
			return true;
		}

		return false;
	}

	/**
	 * Called every tick while the item is in a player's inventory (including while worn).
	 *
	 * @param stack      The ItemStack of this item
	 * @param world      The entity's world
	 * @param entity     The entity
	 * @param itemSlot   The slot containing this item
	 * @param isSelected Is the entity holding this item?
	 */
	@Override
	public void inventoryTick(final ItemStack stack, final Level world, final Entity entity, final int itemSlot, final boolean isSelected) {
		// If this isn't the server or the entity isn't living, do nothing
		if (world.isClientSide || !(entity instanceof final LivingEntity livingEntity)) {
			return;
		}

		if (livingEntity.getItemBySlot(getEquipmentSlot()) == stack) { // If the item is equipped as armour,
			if (!hasReplacedArmour(stack)) { // And the entity's armour hasn't been replaced,
				replaceArmour(stack, livingEntity); // Replace the entity's armour
			}
		} else if (hasReplacedArmour(stack)) { // Else if the entity's armour has been replaced,
			// Try to restore the entity's armour
			InventoryUtils.forEachEntityInventory(
					entity,
					inventory -> tryRestoreArmour(inventory, itemSlot, stack, livingEntity),
					EntityInventoryType.MAIN, EntityInventoryType.HAND
			).ifPresent(successfulInventoryType ->
					LOGGER.info("Restored saved armour for slot {} of {}'s {} inventory", itemSlot, entity.getName(), successfulInventoryType)
			);
		}
	}

	@Override
	public void appendHoverText(final ItemStack stack, final TooltipContext context, final List<Component> tooltip, final TooltipFlag flag) {
		tooltip.add(Component.translatable(TestMod3Lang.ITEM_DESC_ARMOUR_REPLACEMENT_EQUIP.getTranslationKey()));
		tooltip.add(Component.translatable(TestMod3Lang.ITEM_DESC_ARMOUR_REPLACEMENT_UNEQUIP.getTranslationKey()));
	}

	public record ReplacedArmour(ImmutableList<Entry> replacedArmour) {
		public static Codec<ReplacedArmour> CODEC = RecordCodecBuilder.create(builder ->
				builder.group(

						Entry.CODEC
								.listOf()
								.fieldOf("replaced_armour")
								.forGetter(ReplacedArmour::replacedArmour)

				).apply(builder, ReplacedArmour::new)
		);

		public static StreamCodec<RegistryFriendlyByteBuf, ReplacedArmour> STREAM_CODEC = new StreamCodec<>() {
			@Override
			public ReplacedArmour decode(final RegistryFriendlyByteBuf buffer) {
				final var replacedArmour = buffer.readList((b) -> Entry.STREAM_CODEC.decode(buffer));

				return new ReplacedArmour(ImmutableList.copyOf(replacedArmour));
			}

			@Override
			public void encode(final RegistryFriendlyByteBuf buffer, final ReplacedArmour value) {
				buffer.writeCollection(value.replacedArmour, (b, entry) -> Entry.STREAM_CODEC.encode(buffer, entry));
			}
		};

		public ReplacedArmour(final List<Entry> replacedArmour) {
			this(ImmutableList.copyOf(replacedArmour));
		}

		public record Entry(EquipmentSlot slot, ItemStack replacedArmour) {
			public static Codec<Entry> CODEC = RecordCodecBuilder.create(builder ->
					builder.group(
							EquipmentSlot.CODEC
									.fieldOf("slot")
									.forGetter(Entry::slot),

							ItemStack.CODEC
									.fieldOf("replaced_armour")
									.forGetter(Entry::replacedArmour)
					).apply(builder, Entry::new)
			);

			public static StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
					VanillaCodecs.ARMOR_ITEM_EQUIPMENT_SLOT_STREAM_CODEC,
					Entry::slot,
					ItemStack.STREAM_CODEC,
					Entry::replacedArmour,
					Entry::new
			);
		}
	}
}
