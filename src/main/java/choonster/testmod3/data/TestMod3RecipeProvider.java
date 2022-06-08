package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.advancements.criterion.FluidContainerItemPredicate;
import choonster.testmod3.data.crafting.ingredient.ConditionalIngredientBuilder;
import choonster.testmod3.data.crafting.ingredient.MobSpawnerIngredientBuilder;
import choonster.testmod3.data.crafting.recipe.EnhancedShapedRecipeBuilder;
import choonster.testmod3.data.crafting.recipe.ShapedArmourUpgradeRecipeBuilder;
import choonster.testmod3.data.crafting.recipe.ShapelessCuttingRecipeBuilder;
import choonster.testmod3.data.crafting.recipe.ShapelessFluidContainerRecipeBuilder;
import choonster.testmod3.init.ModFluids;
import choonster.testmod3.init.ModItems;
import choonster.testmod3.util.RegistryUtil;
import choonster.testmod3.world.item.crafting.ingredient.FluidContainerIngredient;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

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
	protected void buildCraftingRecipes(final Consumer<FinishedRecipe> recipeConsumer) {
		// Craft a Dimension Replacement item from a Subscripts item and a Superscripts item
		{
			ShapelessRecipeBuilder.shapeless(ModItems.DIMENSION_REPLACEMENT.get())
					.requires(ModItems.SUBSCRIPTS.get())
					.requires(ModItems.SUPERSCRIPTS.get())
					.unlockedBy("has_subscripts", has(ModItems.SUBSCRIPTS.get()))
					.unlockedBy("has_superscripts", has(ModItems.SUPERSCRIPTS.get()))
					.save(recipeConsumer);
		}

		// Craft a Dimension Replacement item by smelting a Subscripts item
		{
			SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.SUBSCRIPTS.get()), ModItems.DIMENSION_REPLACEMENT.get(), 0.35f, 200)
					.unlockedBy("has_subscripts", has(ModItems.SUBSCRIPTS.get()))
					.save(recipeConsumer, new ResourceLocation(TestMod3.MODID, "dimension_replacement_from_subscripts"));
		}

		// A recipe with a conditional ingredient whose conditions are never met.
		// https://github.com/MinecraftForge/MinecraftForge/issues/4359
		{
			ShapedRecipeBuilder.shaped(Blocks.COBBLESTONE)
					.pattern("Cc")
					.define(
							'C',
							ConditionalIngredientBuilder.conditionalIngredient(Blocks.COBBLESTONE)
									.addCondition(new ResourceLocation("forge", "false"))
									.build()
					)
					.define('c', Blocks.COBBLESTONE)
					.unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE))
					.save(recipeConsumer, new ResourceLocation(TestMod3.MODID, "conditional_ingredient_test"));
		}

		// Craft eight Raw Cod from a Guardian Spawner
		// Test for MobSpawnerIngredientSerializer
		{
			ShapelessRecipeBuilder.shapeless(Items.COD, 8)
					.requires(
							MobSpawnerIngredientBuilder.mobSpawnerIngredient(Blocks.SPAWNER)
									.entity(EntityType.GUARDIAN)
									.build()
					)
					.unlockedBy("has_spawner", has(Blocks.SPAWNER))
					.save(recipeConsumer, new ResourceLocation(TestMod3.MODID, "fish_from_guardian_spawner"));
		}

		// Craft a Guardian Spawner from a Raw Cod surrounded by Sticks
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2424619-help-needed-creating-non-pig-mob-spawners
		{
			final ItemStack guardianSpawner = new ItemStack(Blocks.SPAWNER);
			final CompoundTag blockEntityTag = guardianSpawner.getOrCreateTagElement("BlockEntityTag");

			final CompoundTag spawnData = new CompoundTag();

			spawnData.putString("id", RegistryUtil.getKey(EntityType.GUARDIAN).toString());
			blockEntityTag.put("SpawnData", spawnData);

			final ListTag spawnPotentials = new ListTag();
			blockEntityTag.put("SpawnPotentials", spawnPotentials);

			EnhancedShapedRecipeBuilder.Vanilla.shapedRecipe(guardianSpawner)
					.pattern("SSS")
					.pattern("SCS")
					.pattern("SSS")
					.define('S', Tags.Items.RODS_WOODEN) // Sticks
					.define('C', Items.COD)
					.itemGroup("ungrouped")
					.unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
					.unlockedBy("has_cod", has(Items.COD))
					.save(recipeConsumer, new ResourceLocation(TestMod3.MODID, "guardian_spawner_from_fish_and_sticks"));
		}

		// Upgrade an Iron Helmet to a Golden Helmet while preserving its damage
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2513998-help-needed-creating-crafting-recipe-with-damaged
		{
			ShapedArmourUpgradeRecipeBuilder.shapedArmourUpgradeRecipe(Items.GOLDEN_HELMET)
					.pattern("GGG")
					.pattern("GHG")
					.pattern("GGG")
					.define('G', Blocks.GOLD_BLOCK)
					.define('H', Items.IRON_HELMET)
					.unlockedBy("has_gold_block", has(Blocks.GOLD_BLOCK))
					.unlockedBy("has_iron_helmet", has(Items.IRON_HELMET))
					.save(recipeConsumer, new ResourceLocation(TestMod3.MODID, "golden_helmet_from_iron_helmet"));
		}

		// Cut an Oak Log into two Oak Planks with a Cutting Axe, damaging the axe
		{
			ShapelessRecipeBuilder.shapeless(Blocks.OAK_PLANKS, 2)
					.group(new ResourceLocation("minecraft", "planks").toString())
					.requires(ModItems.WOODEN_AXE.get())
					.requires(Blocks.OAK_LOG)
					.unlockedBy("has_axe", has(ModItems.WOODEN_AXE.get()))
					.unlockedBy("has_log", has(Blocks.OAK_LOG))
					.save(recipeConsumer, new ResourceLocation(TestMod3.MODID, "oak_planks_with_mod_axe"));
		}

		// Cut an Oak Log into two Oak Planks with a Wooden Axe, damaging the axe
		{
			ShapelessCuttingRecipeBuilder.shapelessCuttingRecipe(Blocks.OAK_PLANKS, 2)
					.group(new ResourceLocation("minecraft", "planks").toString())
					.requires(Items.WOODEN_AXE)
					.requires(Blocks.OAK_LOG)
					.unlockedBy("has_axe", has(Items.WOODEN_AXE))
					.unlockedBy("has_log", has(Blocks.OAK_LOG))
					.save(recipeConsumer, new ResourceLocation(TestMod3.MODID, "oak_planks_with_vanilla_axe"));
		}

		// Craft Cobblestone from three Buckets of Static Gas
		{
			final FluidStack staticGas = new FluidStack(ModFluids.STATIC_GAS.getStill().get(), FluidAttributes.BUCKET_VOLUME);
			final FluidContainerIngredient staticGasContainer = FluidContainerIngredient.fromFluidStack(staticGas);

			ShapelessFluidContainerRecipeBuilder.shapelessFluidContainerRecipe(Blocks.COBBLESTONE)
					.requires(staticGasContainer)
					.requires(staticGasContainer)
					.requires(staticGasContainer)
					.unlockedBy("has_static_gas_container", inventoryTrigger(
							FluidContainerItemPredicate.Builder.create()
									.fluid(staticGas.getFluid())
									.amount(MinMaxBounds.Ints.atLeast(staticGas.getAmount()))
									.build()
					))
					.save(recipeConsumer, new ResourceLocation(TestMod3.MODID, "cobblestone_from_static_gas"));
		}
	}

	@Override
	public String getName() {
		return "TestMod3Recipes";
	}
}
