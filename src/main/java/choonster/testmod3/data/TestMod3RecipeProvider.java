package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.advancements.criterion.ItemFluidContainerPredicate;
import choonster.testmod3.data.crafting.ingredient.ConditionalIngredientBuilder;
import choonster.testmod3.data.crafting.ingredient.MobSpawnerIngredientBuilder;
import choonster.testmod3.data.crafting.recipe.BaseShapedRecipeBuilder;
import choonster.testmod3.data.crafting.recipe.ShapedArmourUpgradeRecipeBuilder;
import choonster.testmod3.data.crafting.recipe.ShapelessCuttingRecipeBuilder;
import choonster.testmod3.data.crafting.recipe.ShapelessFluidContainerRecipeBuilder;
import choonster.testmod3.init.ModDataComponentPredicates;
import choonster.testmod3.init.ModFluids;
import choonster.testmod3.init.ModItems;
import choonster.testmod3.util.RegistryUtil;
import choonster.testmod3.world.item.crafting.ingredient.FluidContainerIngredient;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.FalseCondition;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Generates this mod's recipes.
 *
 * @author Choonster
 */
public class TestMod3RecipeProvider extends RecipeProvider {
	private static final Logger LOGGER = LogUtils.getLogger();

	private final HolderGetter<Item> items;

	public TestMod3RecipeProvider(final HolderLookup.Provider registries, final RecipeOutput output) {
		super(registries, output);
		items = registries.lookupOrThrow(Registries.ITEM);
	}

	@Override
	protected void buildRecipes() {
		// Craft a Dimension Replacement item from a Subscripts item and a Superscripts item
		{
			shapeless(RecipeCategory.MISC, ModItems.DIMENSION_REPLACEMENT.get())
					.requires(ModItems.SUBSCRIPTS.get())
					.requires(ModItems.SUPERSCRIPTS.get())
					.unlockedBy("has_subscripts", has(ModItems.SUBSCRIPTS.get()))
					.unlockedBy("has_superscripts", has(ModItems.SUPERSCRIPTS.get()))
					.save(output);
		}

		// Craft a Dimension Replacement item by smelting a Subscripts item
		{
			SimpleCookingRecipeBuilder.smelting(
							Ingredient.of(ModItems.SUBSCRIPTS.get()),
							RecipeCategory.MISC,
							CookingBookCategory.MISC,
							ModItems.DIMENSION_REPLACEMENT.get(),
							0.35f,
							200
					)
					.unlockedBy("has_subscripts", has(ModItems.SUBSCRIPTS.get()))
					.save(output, key("dimension_replacement_from_subscripts"));
		}

		// A recipe with a conditional ingredient whose conditions are never met.
		// https://github.com/MinecraftForge/MinecraftForge/issues/4359
		{
			shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE)
					.pattern("Cc")
					.define(
							'C',
							ConditionalIngredientBuilder.conditionalIngredient(Blocks.COBBLESTONE)
									.condition(FalseCondition.INSTANCE)
									.build()
					)
					.define('c', Blocks.COBBLESTONE)
					.unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE))
					.save(output, key("conditional_ingredient_test"));
		}

		// A recipe whose conditions are never met
		{
			final var category = RecipeCategory.BUILDING_BLOCKS;
			final var key = key("conditional_recipe_test");
			final var id = key.identifier();

			ConditionalRecipe.builder()
					.condition(FalseCondition.INSTANCE)
					.recipe(recipeOutput ->
							shapeless(category, Blocks.OAK_LOG)
									.requires(Items.WOODEN_AXE)
									.requires(Items.WOODEN_AXE)
									.unlockedBy("has_axe", has(Items.WOODEN_AXE))
									.save(recipeOutput, key)
					)
					.advancement(id.withPrefix("recipes/" + category.getFolderName() + "/"))
					.save(output, id);
		}

		// Craft eight Raw Cod from a Guardian Spawner
		// Test for MobSpawnerIngredient
		{
			shapeless(RecipeCategory.FOOD, Items.COD, 8)
					.requires(
							MobSpawnerIngredientBuilder.mobSpawnerIngredient(Blocks.SPAWNER)
									.entity(EntityType.GUARDIAN)
									.build()
					)
					.unlockedBy("has_spawner", has(Blocks.SPAWNER))
					.save(output, key("fish_from_guardian_spawner"));
		}

		// Craft a Guardian Spawner from a Raw Cod surrounded by Sticks
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2424619-help-needed-creating-non-pig-mob-spawners
		try (final var problems = new ProblemReporter.ScopedCollector(LOGGER)) {
			final var blockEntityOutput = TagValueOutput.createWithContext(problems, registries);

			final var blockEntityType = BlockEntityType.MOB_SPAWNER;
			BlockEntity.addEntityType(blockEntityOutput, blockEntityType);

			final var entityToSpawn = new CompoundTag();
			entityToSpawn.putString("id", RegistryUtil.getKey(EntityType.GUARDIAN).toString());

			final var spawnData = new SpawnData(
					entityToSpawn,
					Optional.empty(),
					Optional.empty()
			);

			blockEntityOutput.store("SpawnData", SpawnData.CODEC, spawnData);
			blockEntityOutput.childrenList("SpawnPotentials");

			final var components = DataComponentPatch.builder()
					.set(
							DataComponents.BLOCK_ENTITY_DATA,
							TypedEntityData.of(blockEntityType, blockEntityOutput.buildResult())
					)
					.build();

			final var guardianSpawner = new ItemStackTemplate(
					Blocks.SPAWNER.asItem(),
					components
			);

			shaped(RecipeCategory.MISC, guardianSpawner)
					.pattern("SSS")
					.pattern("SCS")
					.pattern("SSS")
					.define('S', Tags.Items.RODS_WOODEN) // Sticks
					.define('C', Items.COD)
					.unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
					.unlockedBy("has_cod", has(Items.COD))
					.save(output, key("guardian_spawner_from_fish_and_sticks"));
		}

		// Upgrade an Iron Helmet to a Golden Helmet while preserving its damage
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2513998-help-needed-creating-crafting-recipe-with-damaged
		{
			shapedArmourUpgrade(RecipeCategory.COMBAT, Items.GOLDEN_HELMET)
					.pattern("GGG")
					.pattern("GHG")
					.pattern("GGG")
					.define('G', Blocks.GOLD_BLOCK)
					.define('H', Items.IRON_HELMET)
					.unlockedBy("has_gold_block", has(Blocks.GOLD_BLOCK))
					.unlockedBy("has_iron_helmet", has(Items.IRON_HELMET))
					.save(output, key("golden_helmet_from_iron_helmet"));
		}

		// Cut an Oak Log into two Oak Planks with a Cutting Axe, damaging the axe
		{
			shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_PLANKS, 2)
					.group(Identifier.withDefaultNamespace("planks").toString())
					.requires(ModItems.WOODEN_AXE.get())
					.requires(Blocks.OAK_LOG)
					.unlockedBy("has_axe", has(ModItems.WOODEN_AXE.get()))
					.unlockedBy("has_log", has(Blocks.OAK_LOG))
					.save(output, key("oak_planks_with_mod_axe"));
		}

		// Cut an Oak Log into two Oak Planks with a Wooden Axe, damaging the axe
		{
			shapelessCuttingRecipe(RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_PLANKS, 2)
					.group(Identifier.withDefaultNamespace("planks").toString())
					.requires(Items.WOODEN_AXE)
					.requires(Blocks.OAK_LOG)
					.unlockedBy("has_axe", has(Items.WOODEN_AXE))
					.unlockedBy("has_log", has(Blocks.OAK_LOG))
					.save(output, key("oak_planks_with_vanilla_axe"));
		}

		// Craft Cobblestone from three Buckets of Static Gas
		{
			final var staticGas = new FluidStack(ModFluids.STATIC_GAS.getStill().get(), FluidType.BUCKET_VOLUME);
			final var staticGasContainer = FluidContainerIngredient.fromFluidStack(staticGas);

			shapelessFluidContainer(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE)
					.requires(staticGasContainer)
					.requires(staticGasContainer)
					.requires(staticGasContainer)
					.unlockedBy("has_static_gas_bucket", has(ModFluids.STATIC_GAS.getBucket().get()))
					.unlockedBy("has_static_gas_container", inventoryTrigger(
							ItemPredicate.Builder.item().withComponents(
									DataComponentMatchers.Builder.components()
											.partial(
													ModDataComponentPredicates.FLUID_CONTAINER.get(),
													ItemFluidContainerPredicate.Builder.create()
															.of(staticGas.getFluid())
															.withAmount(MinMaxBounds.Ints.atLeast(staticGas.getAmount()))
															.build()
											)
											.build()
							)
					))
					.save(output, key("cobblestone_from_static_gas"));
		}
	}


	private static ResourceKey<Recipe<?>> key(final String name) {
		return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(TestMod3.MODID, name));
	}

	private BaseShapedRecipeBuilder.Shaped shaped(
			final RecipeCategory category,
			final ItemStackTemplate result
	) {
		return BaseShapedRecipeBuilder.Shaped.shapedRecipe(items, category, result);
	}

	private ShapedArmourUpgradeRecipeBuilder shapedArmourUpgrade(
			final RecipeCategory category,
			final Item result
	) {
		return ShapedArmourUpgradeRecipeBuilder.shapedArmourUpgradeRecipe(items, category, result);
	}

	private ShapelessCuttingRecipeBuilder shapelessCuttingRecipe(
			final RecipeCategory category,
			final ItemLike result,
			final int count
	) {
		return ShapelessCuttingRecipeBuilder.shapelessCuttingRecipe(items, category, result, count);
	}

	private ShapelessFluidContainerRecipeBuilder shapelessFluidContainer(
			final RecipeCategory category,
			final ItemLike result
	) {
		return ShapelessFluidContainerRecipeBuilder.shapelessFluidContainerRecipe(items, category, result);
	}

	public static class Runner extends RecipeProvider.Runner {
		public Runner(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
			super(output, lookupProvider);
		}

		@Override
		protected RecipeProvider createRecipeProvider(final HolderLookup.Provider registries, final RecipeOutput output) {
			return new TestMod3RecipeProvider(registries, output);
		}

		@Override
		public String getName() {
			return "TestMod3 Recipes";
		}
	}
}
