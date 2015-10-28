package com.choonster.testmod3.init;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.recipe.ShapedArmourUpgradeRecipe;
import com.choonster.testmod3.recipe.ShapelessCuttingRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeFireworks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;

import java.util.Iterator;
import java.util.List;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;
import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPELESS;

public class ModRecipes {
	public static void registerRecipes() {
		registerRecipeClasses();
		addCraftingRecipes();
	}

	private static void registerRecipeClasses() {
		RecipeSorter.register("testmod3:shapelesscutting", ShapelessCuttingRecipe.class, SHAPELESS, "after:minecraft:shapeless");
		RecipeSorter.register("testmod3:shapedarmourupgrade", ShapedArmourUpgradeRecipe.class, SHAPED, "after:forge:shapedore before:minecraft:shapeless");
	}

	private static void addCraftingRecipes() {
		GameRegistry.addRecipe(new ShapelessCuttingRecipe(new ItemStack(Blocks.planks, 2, BlockPlanks.EnumType.OAK.getMetadata()), new ItemStack(Items.wooden_axe, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.OAK.getMetadata())));

		// Upgrade an Iron Helment to a Golden Helmet while preserving its damage - http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2513998-help-needed-creating-crafting-recipe-with-damaged
		GameRegistry.addRecipe(new ShapedArmourUpgradeRecipe(Items.golden_helmet, "AAA", "ABA", "AAA", 'A', Blocks.gold_block, 'B', new ItemStack(Items.iron_helmet, 1, OreDictionary.WILDCARD_VALUE)));

		// Recipe for Guardian Spawner - http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2424619-help-needed-creating-non-pig-mob-spawners
		ItemStack guardianSpawner = new ItemStack(Blocks.mob_spawner);
		NBTTagCompound tileEntityData = guardianSpawner.getSubCompound("BlockEntityTag", true);
		tileEntityData.setString("EntityId", "Guardian");

		GameRegistry.addRecipe(guardianSpawner, "SSS", "SFS", "SSS", 'S', Items.stick, 'F', Items.fish);
	}

	public static void removeCraftingRecipes() {
		removeRecipeClass(RecipeFireworks.class);
		removeRecipe(Items.dye);
		removeRecipe(Blocks.stained_hardened_clay);
	}

	private static void removeRecipe(Block output) {
		removeRecipe(Item.getItemFromBlock(output));
	}

	/**
	 * Remove all recipes with the specified output Item.
	 * <p>
	 * Adapted from Rohzek's code in this post:
	 * http://www.minecraftforge.net/forum/index.php/topic,33631.0.html
	 *
	 * @param output The output Item
	 */
	@SuppressWarnings("unchecked")
	private static void removeRecipe(Item output) {
		int recipesRemoved = 0;

		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		Iterator<IRecipe> remover = recipes.iterator();

		while (remover.hasNext()) {

			ItemStack itemstack = remover.next().getRecipeOutput();

			// If the recipe's output Item is the specified Item,
			if (itemstack != null && itemstack.getItem() == output) {
				// Remove the recipe
				remover.remove();
				recipesRemoved++;
			}
		}

		Logger.info("Removed %d recipes for %s", recipesRemoved, Item.itemRegistry.getNameForObject(output));
	}

	/**
	 * Remove all recipes that are instances of the specified class.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforge.net/forum/index.php/topic,33631.0.html
	 *
	 * @param recipeClass The recipe class
	 */
	@SuppressWarnings("unchecked")
	private static void removeRecipeClass(Class<? extends IRecipe> recipeClass) {
		int recipesRemoved = 0;

		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		Iterator<IRecipe> remover = recipes.iterator();

		while (remover.hasNext()) {
			// If the recipe is an instance of the specified class,
			if (recipeClass.isInstance(remover.next())) {
				// Remove the recipe
				remover.remove();
				recipesRemoved++;
			}
		}

		Logger.info("Removed %d recipes for %s", recipesRemoved, recipeClass);
	}
}
