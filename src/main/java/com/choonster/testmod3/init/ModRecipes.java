package com.choonster.testmod3.init;

import com.choonster.testmod3.recipe.ShapedArmourUpgradeRecipe;
import com.choonster.testmod3.recipe.ShapelessCuttingRecipe;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;

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
		NBTTagCompound tileEntityData = new NBTTagCompound();
		tileEntityData.setString("EntityId", "Guardian");

		NBTTagCompound stackTagCompound = new NBTTagCompound();
		stackTagCompound.setTag("BlockEntityTag", tileEntityData);

		ItemStack guardianSpawner = new ItemStack(Blocks.mob_spawner);
		guardianSpawner.setTagCompound(stackTagCompound);

		GameRegistry.addRecipe(guardianSpawner, "SSS", "SFS", "SSS", 'S', Items.stick, 'F', Items.fish);
	}
}
