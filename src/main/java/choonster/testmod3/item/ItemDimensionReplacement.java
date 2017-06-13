package choonster.testmod3.item;

import choonster.testmod3.Logger;
import choonster.testmod3.util.InventoryUtils;
import choonster.testmod3.util.InventoryUtils.EntityInventoryType;
import choonster.testmod3.util.ItemStackUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An item that's converted to another item when crafted in specific dimension types.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2662868-furnace-recipes
 *
 * @author Choonster
 */
public class ItemDimensionReplacement extends ItemTestMod3 {
	/**
	 * The NBT key used to indicate that the replacement logic has been run.
	 * <p>
	 * This is needed to ensure that items crafted in a dimension without a replacement don't get replaced as soon as the player enters a dimension with a replacement.
	 */
	private static final String KEY_REPLACED = "Replaced";

	/**
	 * The replacement {@link ItemStack} for each {@link DimensionType}.
	 */
	private final Map<DimensionType, ItemStack> replacements = new HashMap<>();

	public ItemDimensionReplacement(final String itemName) {
		super(itemName);
	}

	/**
	 * Add a replacement for this item.
	 *
	 * @param dimensionType The dimension type
	 * @param itemStack     The replacement
	 */
	public void addReplacement(final DimensionType dimensionType, final ItemStack itemStack) {
		replacements.put(dimensionType, itemStack);
	}

	/**
	 * Does the specified {@link World} have a replacement?
	 *
	 * @param world The World
	 * @return Does the World have a replacement?
	 */
	private boolean hasReplacement(@Nullable final World world) {
		return world != null && replacements.containsKey(world.provider.getDimensionType());
	}

	/**
	 * Get the replacement for the specified {@link World}.
	 *
	 * @param world The World
	 * @return The replacement
	 */
	private ItemStack getReplacement(final World world) {
		return replacements.get(world.provider.getDimensionType());
	}

	@Override
	public void onUpdate(final ItemStack stack, final World worldIn, final Entity entityIn, final int itemSlot, final boolean isSelected) {
		final NBTTagCompound stackTagCompound = ItemStackUtils.getOrCreateTagCompound(stack);

		if (!stackTagCompound.getBoolean(KEY_REPLACED)) { // If the replacement logic hasn't been run,
			stackTagCompound.setBoolean(KEY_REPLACED, true); // Mark it as run

			if (hasReplacement(worldIn)) { // If there's a replacement for this dimension
				final ItemStack replacement = getReplacement(worldIn).copy(); // Get it
				replacement.setCount(stack.getCount()); // Copy the stack size from this item

				// Try to replace this item
				final EntityInventoryType successfulInventoryType = InventoryUtils.forEachEntityInventory(
						entityIn,
						inventory -> tryReplaceItem(inventory, itemSlot, stack, replacement),
						EntityInventoryType.MAIN, EntityInventoryType.HAND
				);

				if (successfulInventoryType != null) {
					Logger.info("Replaced item in slot %d of %s's %s inventory with %s", itemSlot, entityIn.getName(), successfulInventoryType, replacement.getDisplayName());
				}
			}
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
	private boolean tryReplaceItem(final IItemHandler inventory, final int slot, final ItemStack stackToReplace, final ItemStack replacementStack) {
		if (slot < inventory.getSlots() && inventory.getStackInSlot(slot) == stackToReplace && !inventory.extractItem(slot, stackToReplace.getCount(), true).isEmpty()) {
			inventory.extractItem(slot, stackToReplace.getCount(), false);
			inventory.insertItem(slot, replacementStack, false);
			return true;
		}

		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(final ItemStack stack, @Nullable final World world, final List<String> tooltip, final ITooltipFlag advanced) {
		if (hasReplacement(world)) {
			tooltip.add(I18n.format("item.testmod3:dimension_replacement.replacement.desc", getReplacement(world).getDisplayName()));
		} else {
			tooltip.add(I18n.format("item.testmod3:dimension_replacement.no_replacement.desc"));
		}
	}
}
