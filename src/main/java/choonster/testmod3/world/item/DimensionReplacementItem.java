package choonster.testmod3.world.item;

import choonster.testmod3.init.ModDataComponents;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.InventoryUtils;
import choonster.testmod3.util.InventoryUtils.EntityInventoryType;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.items.IItemHandler;
import org.slf4j.Logger;

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
	 * The replacement {@link ItemStack} for each {@link DimensionType}.
	 */
	private final Map<ResourceKey<DimensionType>, Supplier<ItemStack>> replacements;

	public DimensionReplacementItem(
			final Map<ResourceKey<DimensionType>, Supplier<ItemStack>> replacements,
			final Properties properties
	) {
		super(properties);

		this.replacements = ImmutableMap.copyOf(
				replacements
						.entrySet()
						.stream()
						.map(entry -> Pair.of(entry.getKey(), Lazy.of(entry.getValue())))
						.collect(Collectors.toMap(Pair::getFirst, Pair::getSecond))
		);

		MinecraftForge.EVENT_BUS.addListener(this::itemTooltip);
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
				.lookupOrThrow(Registries.DIMENSION_TYPE)
				.getResourceKey(level.dimensionType())
				.map(replacements::get)
				.map(Supplier::get);
	}

	@Override
	public void inventoryTick(final ItemStack stack, final Level level, final Entity entity, final int itemSlot, final boolean isSelected) {
		if (level.isClientSide) {
			return;
		}

		if (!stack.has(ModDataComponents.DIMENSION_REPLACER_REPLACED.get())) { // If the replacement logic hasn't been run,
			stack.set(ModDataComponents.DIMENSION_REPLACER_REPLACED.get(), Unit.INSTANCE); // Mark it as run

			getReplacement(level).ifPresent(replacement -> { // If there's a replacement for this dimension's type
				final var replacementCopy = replacement.copy();

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

	private void itemTooltip(final ItemTooltipEvent event) {
		final var stack = event.getItemStack();
		final var player = event.getEntity();

		if (player == null || stack.getItem() != this) {
			return;
		}

		event.getToolTip().add(
				getReplacement(player.level())
						.map(replacement -> Component.translatable(TestMod3Lang.ITEM_DESC_DIMENSION_REPLACEMENT_REPLACEMENT.getTranslationKey(), replacement.getHoverName()))
						.orElseGet(() -> Component.translatable(TestMod3Lang.ITEM_DESC_DIMENSION_REPLACEMENT_NO_REPLACEMENT.getTranslationKey()))
		);
	}
}
