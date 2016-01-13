package com.choonster.testmod3.recipe;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * A shaped recipe class that copies the item damage of the first armour ingredient to the output. The damage is clamped to the output item's damage range.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2513998-help-needed-creating-crafting-recipe-with-damaged
 *
 * @author Choonster
 */
public class ShapedArmourUpgradeRecipe extends ShapedOreRecipe {
	public ShapedArmourUpgradeRecipe(Block result, Object... recipe) {
		super(result, recipe);
	}

	public ShapedArmourUpgradeRecipe(Item result, Object... recipe) {
		super(result, recipe);
	}

	public ShapedArmourUpgradeRecipe(ItemStack result, Object... recipe) {
		super(result, recipe);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack output = super.getCraftingResult(inv); // Get the default output

		for (int i = 0; i < inv.getSizeInventory(); i++) { // For each slot in the crafting inventory,
			ItemStack ingredient = inv.getStackInSlot(i); // Get the ingredient in the slot

			if (ingredient != null && ingredient.getItem() instanceof ItemArmor) { // If it's an armour item,
				// Clone its item damage, clamping it to the output's damage range
				int newDamage = MathHelper.clamp_int(ingredient.getItemDamage(), 0, output.getMaxDamage());
				output.setItemDamage(newDamage);
				break; // Break now
			}
		}

		return output; // Return the modified output
	}
}
