package com.choonster.testmod3.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.common.ForgeHooks;

import java.util.Arrays;
import java.util.Random;

public class ShapelessCuttingRecipe extends ShapelessRecipes {

	private Random random = new Random();

	public ShapelessCuttingRecipe(ItemStack output, ItemStack... input) {
		super(output, Arrays.asList(input));
	}

	private ItemStack damageAxe(ItemStack stack) {
		if (stack.attemptDamageItem(1, random)) {
			return null;
		}

		return stack;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inventoryCrafting) {
		ItemStack[] remainingItems = new ItemStack[inventoryCrafting.getSizeInventory()];

		for (int i = 0; i < remainingItems.length; ++i) {
			ItemStack itemstack = inventoryCrafting.getStackInSlot(i);

			if (itemstack != null && itemstack.getItem() instanceof ItemAxe) {
				remainingItems[i] = damageAxe(itemstack.copy());
			} else {
				remainingItems[i] = ForgeHooks.getContainerItem(itemstack);
			}
		}

		return remainingItems;
	}
}
