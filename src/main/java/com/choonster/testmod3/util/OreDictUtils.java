package com.choonster.testmod3.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictUtils {
	public static final OreDictUtils INSTANCE = new OreDictUtils();

	/**
	 * Is the ItemStack registered under the specified ore name?
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforge.net/forum/index.php/topic,34118.0.html
	 *
	 * @param stack   The ItemStack
	 * @param oreName The ore name
	 * @return Is the ItemStack registered?
	 */
	public boolean isItemStackRegisteredForName(ItemStack stack, String oreName) {
		final int[] oreIDs = OreDictionary.getOreIDs(stack);

		for (final int oreID : oreIDs) {
			if (oreName.equals(OreDictionary.getOreName(oreID))) {
				return true;
			}
		}

		return false;
	}
}
