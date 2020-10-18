package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.data.crafting.ingredient.ConditionalIngredientBuilder;
import choonster.testmod3.data.crafting.ingredient.MobSpawnerIngredientBuilder;
import choonster.testmod3.data.crafting.recipe.EnhancedShapedRecipeBuilder;
import choonster.testmod3.data.crafting.recipe.ShapedArmourUpgradeRecipeBuilder;
import choonster.testmod3.data.crafting.recipe.ShapelessCuttingRecipeBuilder;
import choonster.testmod3.init.ModItems;
import com.google.common.base.Preconditions;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

/**
 * Generates this mod's recipes.
 *
 * @author Choonster
 */
public class TestMod3RecipeProvider extends RecipeProvider {
	public TestMod3RecipeProvider(final DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void registerRecipes(final Consumer<IFinishedRecipe> recipeConsumer) {
		// Craft a Dimension Replacement item from a Subscripts item and a Superscripts item
		{
			ShapelessRecipeBuilder.shapelessRecipe(ModItems.DIMENSION_REPLACEMENT.get())
					.addIngredient(ModItems.SUBSCRIPTS.get())
					.addIngredient(ModItems.SUPERSCRIPTS.get())
					.addCriterion("has_subscripts", hasItem(ModItems.SUBSCRIPTS.get()))
					.addCriterion("has_superscripts", hasItem(ModItems.SUPERSCRIPTS.get()))
					.build(recipeConsumer);
		}

		// Craft a Dimension Replacement item by smelting a Subscripts item
		{
			CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModItems.SUBSCRIPTS.get()), ModItems.DIMENSION_REPLACEMENT.get(), 0.35f, 200)
					.addCriterion("has_subscripts", hasItem(ModItems.SUBSCRIPTS.get()))
					.build(recipeConsumer, new ResourceLocation(TestMod3.MODID, "dimension_replacement_from_subscripts"));
		}

		// A recipe with a conditional ingredient whose conditions are never met.
		// https://github.com/MinecraftForge/MinecraftForge/issues/4359
		{
			ShapedRecipeBuilder.shapedRecipe(Blocks.COBBLESTONE)
					.patternLine("Cc")
					.key(
							'C',
							ConditionalIngredientBuilder.conditionalIngredient(Blocks.COBBLESTONE)
									.addCondition(new ResourceLocation("forge", "false"))
									.build()
					)
					.key('c', Blocks.COBBLESTONE)
					.addCriterion("has_cobblestone", hasItem(Blocks.COBBLESTONE))
					.build(recipeConsumer, new ResourceLocation(TestMod3.MODID, "conditional_ingredient_test"));
		}

		// Craft eight Raw Cod from a Guardian Spawner
		// Test for MobSpawnerIngredientSerializer
		{
			ShapelessRecipeBuilder.shapelessRecipe(Items.COD, 8)
					.addIngredient(
							MobSpawnerIngredientBuilder.mobSpawnerIngredient(Blocks.SPAWNER)
									.entity(EntityType.GUARDIAN)
									.build()
					)
					.addCriterion("has_spawner", hasItem(Blocks.SPAWNER))
					.build(recipeConsumer, new ResourceLocation(TestMod3.MODID, "fish_from_guardian_spawner"));
		}

		// Craft a Guardian Spawner from a Raw Cod surrounded by Sticks
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2424619-help-needed-creating-non-pig-mob-spawners
		{
			final ItemStack guardianSpawner = new ItemStack(Blocks.SPAWNER);
			final CompoundNBT blockEntityTag = guardianSpawner.getOrCreateChildTag("BlockEntityTag");

			final CompoundNBT spawnData = new CompoundNBT();

			spawnData.putString("id", Preconditions.checkNotNull(EntityType.GUARDIAN.getRegistryName()).toString());
			blockEntityTag.put("SpawnData", spawnData);

			final ListNBT spawnPotentials = new ListNBT();
			blockEntityTag.put("SpawnPotentials", spawnPotentials);

			EnhancedShapedRecipeBuilder.shapedRecipe(guardianSpawner)
					.patternLine("SSS")
					.patternLine("SCS")
					.patternLine("SSS")
					.key('S', Tags.Items.RODS_WOODEN) // Sticks
					.key('C', Items.COD)
					.setItemGroup("ungrouped")
					.addCriterion("has_stick", hasItem(Tags.Items.RODS_WOODEN))
					.addCriterion("has_cod", hasItem(Items.COD))
					.build(recipeConsumer, new ResourceLocation(TestMod3.MODID, "guardian_spawner_from_fish_and_sticks"));
		}

		// Upgrade an Iron Helmet to a Golden Helmet while preserving its damage
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2513998-help-needed-creating-crafting-recipe-with-damaged
		{
			ShapedArmourUpgradeRecipeBuilder.shapedArmourUpgradeRecipe(Items.GOLDEN_HELMET)
					.patternLine("GGG")
					.patternLine("GHG")
					.patternLine("GGG")
					.key('G', Blocks.GOLD_BLOCK)
					.key('H', Items.IRON_HELMET)
					.addCriterion("has_gold_block", hasItem(Blocks.GOLD_BLOCK))
					.addCriterion("has_iron_helmet", hasItem(Items.IRON_HELMET))
					.build(recipeConsumer, new ResourceLocation(TestMod3.MODID, "golden_helmet_from_iron_helmet"));
		}

		// Cut an Oak Log into two Oak Planks with a Cutting Axe, damaging the axe
		{
			ShapelessRecipeBuilder.shapelessRecipe(Blocks.OAK_PLANKS, 2)
					.setGroup(new ResourceLocation("minecraft", "planks").toString())
					.addIngredient(ModItems.WOODEN_AXE.get())
					.addIngredient(Blocks.OAK_LOG)
					.addCriterion("has_axe", hasItem(ModItems.WOODEN_AXE.get()))
					.addCriterion("has_log", hasItem(Blocks.OAK_LOG))
					.build(recipeConsumer, new ResourceLocation(TestMod3.MODID, "oak_planks_with_mod_axe"));
		}

		// Cut an Oak Log into two Oak Planks with a Wooden Axe, damaging the axe
		{
			ShapelessCuttingRecipeBuilder.shapelessCuttingRecipe(Blocks.OAK_PLANKS, 2)
					.setGroup(new ResourceLocation("minecraft", "planks").toString())
					.addIngredient(Items.WOODEN_AXE)
					.addIngredient(Blocks.OAK_LOG)
					.addCriterion("has_axe", hasItem(Items.WOODEN_AXE))
					.addCriterion("has_log", hasItem(Blocks.OAK_LOG))
					.build(recipeConsumer, new ResourceLocation(TestMod3.MODID, "oak_planks_with_vanilla_axe"));
		}
	}

	@Override
	public String getName() {
		return "TestMod3Recipes";
	}
}
