package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.InventoryUtils;
import choonster.testmod3.util.InventoryUtils.EntityInventoryType;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * An item that's converted to another item when crafted in specific dimension types.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2662868-furnace-recipes
 *
 * @author Choonster
 */
public class DimensionReplacementItem extends Item {
	private static final Logger LOGGER = LogUtils.getLogger();

	/**
	 * The NBT key used to indicate that the replacement logic has been run.
	 * <p>
	 * This is needed to ensure that items crafted in a dimension without a replacement don't get replaced as soon as the player enters a dimension with a replacement.
	 */
	private static final String KEY_REPLACED = "Replaced";

	/**
	 * The replacement {@link ItemStack} for each {@link DimensionType}.
	 */
	private final Map<ResourceKey<DimensionType>, Supplier<ItemStack>> replacements;

	public DimensionReplacementItem(final Properties properties, final Map<ResourceKey<DimensionType>, Supplier<ItemStack>> replacements) {
		super(properties);

		this.replacements = ImmutableMap.copyOf(
				replacements
						.entrySet()
						.stream()
						.map(entry -> Pair.of(entry.getKey(), Lazy.of(entry.getValue())))
						.collect(Collectors.toMap(Pair::getFirst, Pair::getSecond))
		);
	}

	/**
	 * Get the replacement for the specified {@link Level}, if any.
	 *
	 * @param level The level
	 * @return The optional replacement
	 */
	private Optional<ItemStack> getReplacement(final Level level) {
		return level
				.registryAccess()
				.registryOrThrow(Registries.DIMENSION_TYPE)
				.getResourceKey(level.dimensionType())
				.map(replacements::get)
				.map(Supplier::get);
	}

	@Override
	public void inventoryTick(final ItemStack stack, final Level world, final Entity entity, final int itemSlot, final boolean isSelected) {
		if (world.isClientSide) {
			return;
		}

		final CompoundTag stackTagCompound = stack.getOrCreateTag();

		if (!stackTagCompound.getBoolean(KEY_REPLACED)) { // If the replacement logic hasn't been run,
			stackTagCompound.putBoolean(KEY_REPLACED, true); // Mark it as run

			getReplacement(world).ifPresent(replacement -> { // If there's a replacement for this dimension's type
				final ItemStack replacementCopy = replacement.copy();
				replacementCopy.setCount(stack.getCount()); // Copy the stack size from this item

				// Try to replace this item
				InventoryUtils.forEachEntityInventory(
						entity,
						inventory -> tryReplaceItem(inventory, itemSlot, stack, replacementCopy),
						EntityInventoryType.MAIN, EntityInventoryType.HAND
				).ifPresent(successfulInventoryType ->
						LOGGER.info("Replaced item in slot {} of {}'s {} inventory with {}", itemSlot, entity.getName(), successfulInventoryType, replacementCopy.getHoverName())
				);
			});
		}
	}

	/**
	 * Replace the item in the specified inventory slot if the slot contains the specified ItemStack.
	 *
	 * @param inventory        The inventory
	 * @param slot             The inventory slot
	 * @param stackToReplace   The ItemStack to replace
	 * @param replacementStack The replacement ItemStack
	 * @return Was the item replaced?
	 */
	private static boolean tryReplaceItem(final IItemHandler inventory, final int slot, final ItemStack stackToReplace, final ItemStack replacementStack) {
		if (slot < inventory.getSlots() && inventory.getStackInSlot(slot) == stackToReplace && !inventory.extractItem(slot, stackToReplace.getCount(), true).isEmpty()) {
			inventory.extractItem(slot, stackToReplace.getCount(), false);
			inventory.insertItem(slot, replacementStack, false);
			return true;
		}

		return false;
	}

	@Override
	public void appendHoverText(final ItemStack stack, @Nullable final Level world, final List<Component> tooltip, final TooltipFlag flag) {
		if (world == null) {
			return;
		}

		tooltip.add(
				getReplacement(world)
						.map(replacement -> Component.translatable(TestMod3Lang.ITEM_DESC_DIMENSION_REPLACEMENT_REPLACEMENT.getTranslationKey(), replacement.getHoverName()))
						.orElseGet(() -> Component.translatable(TestMod3Lang.ITEM_DESC_DIMENSION_REPLACEMENT_NO_REPLACEMENT.getTranslationKey()))
		);
	}
}
