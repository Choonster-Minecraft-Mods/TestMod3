package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.Constants;
import choonster.testmod3.util.InventoryUtils;
import choonster.testmod3.util.InventoryUtils.EntityInventoryType;
import com.google.common.collect.ImmutableSet;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

	// NBT keys
	private static final String KEY_REPLACED_ARMOUR = "ReplacedArmour";
	private static final String KEY_SLOT = "Slot";
	private static final String KEY_STACK = "Stack";

	/**
	 * The items to replace the other armour with.
	 */
	private final Set<Supplier<ItemStack>> replacementItems;

	public ReplacementArmourItem(final ArmorMaterial material, final EquipmentSlot slot, final Properties properties, final Collection<Supplier<ItemStack>> replacementItems) {
		super(material, slot, properties);

		this.replacementItems = ImmutableSet.copyOf(
				replacementItems
						.stream()
						.map(Lazy::of)
						.collect(Collectors.toSet())
		);
	}

	/**
	 * Get the items to replace the other armour with.
	 *
	 * @return The items to replace the other armour with
	 */
	public Set<Supplier<ItemStack>> getReplacementItems() {
		return replacementItems;
	}

	/**
	 * Has this item replaced the other armour?
	 *
	 * @param stack The ItemStack of this item
	 * @return Has this item replaced the other armour?
	 */
	public static boolean hasReplacedArmour(final ItemStack stack) {
		return stack.getOrCreateTag().contains(KEY_REPLACED_ARMOUR, Tag.TAG_LIST);
	}

	/**
	 * Save the entity's armour and replace it with the replacements defined for this item.
	 *
	 * @param stack  The ItemStack of this item
	 * @param entity The entity
	 */
	private void replaceArmour(final ItemStack stack, final LivingEntity entity) {
		final CompoundTag stackTagCompound = stack.getOrCreateTag();
		final ListTag replacedArmour = new ListTag();

		// Create a mutable copy of the replacements
		final Set<ItemStack> replacements = replacementItems
				.stream()
				.map(Supplier::get)
				.collect(Collectors.toSet());

		Constants.ARMOUR_SLOTS.stream() // For each armour type,
				.filter(equipmentSlot -> equipmentSlot != getSlot()) // If it's not this item's equipment slot,
				.forEach(equipmentSlot -> {
					final Optional<ItemStack> optionalReplacement = replacements.stream()
							.filter(replacementStack -> replacementStack.getItem().canEquip(replacementStack, equipmentSlot, entity))
							.findFirst();

					optionalReplacement.ifPresent(replacement -> { // If there's a replacement for this armour type,
						replacements.remove(replacement); // Don't use it for any other armour type

						// Create a compound tag to store the original and add it to the list of replaced armour
						final CompoundTag compoundNBT = new CompoundTag();
						replacedArmour.add(compoundNBT);
						compoundNBT.putByte(KEY_SLOT, (byte) equipmentSlot.getFilterFlag());

						// If the original item exists, add it to the compound tag
						final ItemStack original = entity.getItemBySlot(equipmentSlot);
						if (!original.isEmpty()) {
							compoundNBT.put(KEY_STACK, original.serializeNBT());
						}

						entity.setItemSlot(equipmentSlot, replacement.copy()); // Equip a copy of the replacement
						LOGGER.info("Equipped replacement {} to {}, replacing {}", replacement, equipmentSlot, original);
					});
				});

		stackTagCompound.put(KEY_REPLACED_ARMOUR, replacedArmour); // Save the replaced armour to the ItemStack
	}

	/**
	 * Restore the entity's saved armour from this item's ItemStack NBT.
	 *
	 * @param stack  The ItemStack of this item
	 * @param entity The entity
	 */
	private void restoreArmour(final ItemStack stack, final LivingEntity entity) {
		final CompoundTag stackTagCompound = stack.getOrCreateTag();
		final ListTag replacedArmour = stackTagCompound.getList(KEY_REPLACED_ARMOUR, Tag.TAG_COMPOUND);

		for (int i = 0; i < replacedArmour.size(); i++) { // For each saved armour item,
			final CompoundTag replacedTagCompound = replacedArmour.getCompound(i);
			final ItemStack original = ItemStack.of(replacedTagCompound.getCompound(KEY_STACK)); // Load the original ItemStack from the NBT

			final EquipmentSlot equipmentSlot = InventoryUtils.getEquipmentSlotFromIndex(replacedTagCompound.getByte(KEY_SLOT)); // Get the armour slot
			final ItemStack current = entity.getItemBySlot(equipmentSlot);

			// Is the item currently in the slot one of the replacements defined for this item?
			final boolean isReplacement = replacementItems
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

		stackTagCompound.remove(KEY_REPLACED_ARMOUR);

		if (stackTagCompound.isEmpty()) {
			stack.setTag(null);
		}
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
	 * Called every tick while the item is worn by a player.
	 *
	 * @param stack  The ItemStack of this item
	 * @param world  The player's world
	 * @param player The player
	 */
	@Override
	public void onArmorTick(final ItemStack stack, final Level world, final Player player) {
		if (!world.isClientSide && !hasReplacedArmour(stack)) { // If this is the server and the player's armour hasn't been replaced,
			replaceArmour(stack, player); // Replace the player's armour
			player.inventoryMenu.broadcastChanges(); // Sync the player's inventory with the client
		}
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
		// If this is the server, the entity is living and the entity's armour has been replaced,
		if (!world.isClientSide && entity instanceof LivingEntity && hasReplacedArmour(stack)) {

			// Try to restore the entity's armour
			InventoryUtils.forEachEntityInventory(
					entity,
					inventory -> tryRestoreArmour(inventory, itemSlot, stack, (LivingEntity) entity),
					EntityInventoryType.MAIN, EntityInventoryType.HAND
			).ifPresent(successfulInventoryType ->
					LOGGER.info("Restored saved armour for slot {} of {}'s {} inventory", itemSlot, entity.getName(), successfulInventoryType)
			);
		}
	}

	@Override
	public void appendHoverText(final ItemStack stack, @Nullable final Level level, final List<Component> tooltip, final TooltipFlag flagIn) {
		tooltip.add(new TranslatableComponent(TestMod3Lang.ITEM_DESC_ARMOUR_REPLACEMENT_EQUIP.getTranslationKey()));
		tooltip.add(new TranslatableComponent(TestMod3Lang.ITEM_DESC_ARMOUR_REPLACEMENT_UNEQUIP.getTranslationKey()));
	}
}
