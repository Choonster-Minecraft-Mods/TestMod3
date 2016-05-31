package com.choonster.testmod3.util;

import com.google.common.collect.Maps;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

	private final Map<EnumDyeColor, String> dyeOreNames;

	{
		final Map<EnumDyeColor, String> map = new HashMap<>();

		map.put(EnumDyeColor.BLACK, "dyeBlack");
		map.put(EnumDyeColor.RED, "dyeRed");
		map.put(EnumDyeColor.GREEN, "dyeGreen");
		map.put(EnumDyeColor.BROWN, "dyeBrown");
		map.put(EnumDyeColor.BLUE, "dyeBlue");
		map.put(EnumDyeColor.PURPLE, "dyePurple");
		map.put(EnumDyeColor.CYAN, "dyeCyan");
		map.put(EnumDyeColor.SILVER, "dyeLightGray");
		map.put(EnumDyeColor.GRAY, "dyeGray");
		map.put(EnumDyeColor.PINK, "dyePink");
		map.put(EnumDyeColor.LIME, "dyeLime");
		map.put(EnumDyeColor.YELLOW, "dyeYellow");
		map.put(EnumDyeColor.LIGHT_BLUE, "dyeLightBlue");
		map.put(EnumDyeColor.MAGENTA, "dyeMagenta");
		map.put(EnumDyeColor.ORANGE, "dyeOrange");
		map.put(EnumDyeColor.WHITE, "dyeWhite");

		dyeOreNames = Maps.immutableEnumMap(map);
	}

	/**
	 * Get the dye colour of the specified item, if any.
	 *
	 * @param stack The item
	 * @return The dye colour, if any
	 */
	public Optional<EnumDyeColor> getDyeColour(ItemStack stack) {
		return dyeOreNames.entrySet().stream()
				.filter(entry -> isItemStackRegisteredForName(stack, entry.getValue()))
				.map(Map.Entry::getKey)
				.findFirst();
	}
}
