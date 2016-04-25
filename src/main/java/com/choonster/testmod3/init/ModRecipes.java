package com.choonster.testmod3.init;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.recipe.ShapedArmourUpgradeRecipe;
import com.choonster.testmod3.recipe.ShapelessCuttingRecipe;
import com.choonster.testmod3.recipe.ShapelessNBTRecipe;
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
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapelessOreRecipe;

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
		RecipeSorter.register("testmod3:shapelessnbt", ShapelessNBTRecipe.class, SHAPELESS, "after:forge:shapelessore");
	}

	private static void addCraftingRecipes() {
		GameRegistry.addRecipe(new ShapelessCuttingRecipe(new ItemStack(Blocks.PLANKS, 2, BlockPlanks.EnumType.OAK.getMetadata()), new ItemStack(Items.WOODEN_AXE, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.OAK.getMetadata())));

		// Upgrade an Iron Helment to a Golden Helmet while preserving its damage - http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2513998-help-needed-creating-crafting-recipe-with-damaged
		GameRegistry.addRecipe(new ShapedArmourUpgradeRecipe(Items.GOLDEN_HELMET, "AAA", "ABA", "AAA", 'A', Blocks.GOLD_BLOCK, 'B', new ItemStack(Items.IRON_HELMET, 1, OreDictionary.WILDCARD_VALUE)));

		// Recipe for Guardian Spawner - http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2424619-help-needed-creating-non-pig-mob-spawners
		final ItemStack guardianSpawner = new ItemStack(Blocks.MOB_SPAWNER);
		final NBTTagCompound tileEntityData = guardianSpawner.getSubCompound("BlockEntityTag", true);
		final NBTTagCompound spawnData = new NBTTagCompound();
		spawnData.setString("id", "Guardian");
		tileEntityData.setTag("SpawnData", spawnData);
		tileEntityData.setTag("SpawnPotentials", new NBTTagList());
		GameRegistry.addRecipe(guardianSpawner, "SSS", "SFS", "SSS", 'S', Items.STICK, 'F', Items.FISH);

		final ItemStack bucketOfStaticGas = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, ModFluids.STATIC_GAS);
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.COBBLESTONE), bucketOfStaticGas, bucketOfStaticGas, bucketOfStaticGas));

		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.DIMENSION_REPLACEMENT), ModItems.SUBSCRIPTS, ModItems.SUPERSCRIPTS);
		GameRegistry.addSmelting(ModItems.SUBSCRIPTS, new ItemStack(ModItems.DIMENSION_REPLACEMENT), 0.35f);
	}

	public static void removeCraftingRecipes() {
		removeRecipeClass(RecipeFireworks.class);
		removeRecipe(Items.DYE);
		removeRecipe(Blocks.STAINED_HARDENED_CLAY);
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
	private static void removeRecipe(Item output) {
		int recipesRemoved = 0;

		final List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		final Iterator<IRecipe> remover = recipes.iterator();

		while (remover.hasNext()) {
			final ItemStack itemstack = remover.next().getRecipeOutput();

			// If the recipe's output Item is the specified Item,
			if (itemstack != null && itemstack.getItem() == output) {
				// Remove the recipe
				remover.remove();
				recipesRemoved++;
			}
		}

		Logger.info("Removed %d recipes for %s", recipesRemoved, output.getRegistryName());
	}

	/**
	 * Remove all recipes that are instances of the specified class.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforge.net/forum/index.php/topic,33631.0.html
	 *
	 * @param recipeClass The recipe class
	 */
	private static void removeRecipeClass(Class<? extends IRecipe> recipeClass) {
		int recipesRemoved = 0;

		final List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		final Iterator<IRecipe> remover = recipes.iterator();

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
