package com.choonster.testmod3.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Utility methods for inventories.
 *
 * @author Choonster
 */
public class InventoryUtils {

	/**
	 * Add the {@link ItemStack} to the player's inventory or drop it on the ground if there's no room.
	 *
	 * @param player The player
	 * @param stack  The ItemStack
	 */
	public static void addOrDropItem(EntityPlayer player, ItemStack stack) {
		if (!player.inventory.addItemStackToInventory(stack)) {
			player.dropPlayerItemWithRandomChoice(stack, false);
		}
	}
}
