package choonster.testmod3.item;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.InventoryUtils;
import choonster.testmod3.util.InventoryUtils.EntityInventoryType;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
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
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * The NBT key used to indicate that the replacement logic has been run.
	 * <p>
	 * This is needed to ensure that items crafted in a dimension without a replacement don't get replaced as soon as the player enters a dimension with a replacement.
	 */
	private static final String KEY_REPLACED = "Replaced";

	/**
	 * The replacement {@link ItemStack} for each {@link DimensionType}.
	 */
	private final Map<RegistryKey<DimensionType>, Supplier<ItemStack>> replacements;

	public DimensionReplacementItem(final Properties properties, final Map<RegistryKey<DimensionType>, Supplier<ItemStack>> replacements) {
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
	 * Get the replacement for the specified {@link World}, if any.
	 *
	 * @param world The World
	 * @return The optional replacement
	 */
	private Optional<ItemStack> getReplacement(final World world) {
		return world
				./* getDynamicRegistries */registryAccess()
				./* getDimensionTypeRegistry */dimensionTypes()
				.getResourceKey(world.dimensionType())
				.map(replacements::get)
				.map(Supplier::get);
	}

	@Override
	public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
		if (world.isClientSide) {
			return;
		}

		final CompoundNBT stackTagCompound = stack.getOrCreateTag();

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
	public void appendHoverText(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
		if (world == null) {
			return;
		}

		tooltip.add(
				getReplacement(world)
						.map(replacement -> new TranslationTextComponent(TestMod3Lang.ITEM_DESC_DIMENSION_REPLACEMENT_REPLACEMENT.getTranslationKey(), replacement.getHoverName()))
						.orElseGet(() -> new TranslationTextComponent(TestMod3Lang.ITEM_DESC_DIMENSION_REPLACEMENT_NO_REPLACEMENT.getTranslationKey()))
		);
	}
}
