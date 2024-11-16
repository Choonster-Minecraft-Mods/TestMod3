package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.item.crafting.ingredient.ConditionalIngredient;
import choonster.testmod3.world.item.crafting.ingredient.FluidContainerIngredient;
import choonster.testmod3.world.item.crafting.ingredient.MobSpawnerIngredient;
import choonster.testmod3.world.item.crafting.ingredient.NeverIngredient;
import choonster.testmod3.world.item.crafting.recipe.EnhancedShapedRecipe;
import choonster.testmod3.world.item.crafting.recipe.ShapedArmourUpgradeRecipe;
import choonster.testmod3.world.item.crafting.recipe.ShapelessCuttingRecipe;
import choonster.testmod3.world.item.crafting.recipe.ShapelessFluidContainerRecipe;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.brewing.BrewingRecipeRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * Manages this mod's recipes and ingredients and removes recipes.
 */
public class ModCrafting {
	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class Brewing {
		/**
		 * Add this mod's brewing recipes.
		 *
		 * @param event The common setup event
		 */
		@SubscribeEvent
		public static void registerBrewingRecipes(final BrewingRecipeRegisterEvent event) {
			addStandardConversionRecipes(event.getBuilder(), ModPotions.TEST, ModPotions.LONG_TEST, ModPotions.STRONG_TEST, ModItems.ARROW);
		}

		/**
		 * Add the standard conversion recipes for the specified {@link Potion}s:
		 * <ul>
		 * <li>Awkward + Ingredient = Standard</li>
		 * <li>Standard + Redstone = Long</li>
		 * <li>Standard + Glowstone = Strong</li>
		 * </ul>
		 *
		 * @param builder        The PotionBrewing builder
		 * @param standardPotion The standard Potion
		 * @param longPotion     The long Potion
		 * @param strongPotion   The strong Potion
		 * @param ingredient     The ingredient
		 */
		private static void addStandardConversionRecipes(
				final PotionBrewing.Builder builder,
				final Holder<Potion> standardPotion,
				final Holder<Potion> longPotion,
				final Holder<Potion> strongPotion,
				final Item ingredient
		) {
			builder.addMix(Potions.AWKWARD, ingredient, standardPotion);
			builder.addMix(standardPotion, Items.REDSTONE, longPotion);
			builder.addMix(standardPotion, Items.GLOWSTONE_DUST, strongPotion);
		}

		/**
		 * Overload of {@link #addStandardConversionRecipes(PotionBrewing.Builder, Holder, Holder, Holder, Item)} that
		 * accepts {@link RegistryObject} parameters for convenience.
		 *
		 * @param builder        The PotionBrewing builder
		 * @param standardPotion The standard Potion
		 * @param longPotion     The long Potion
		 * @param strongPotion   The strong Potion
		 * @param ingredient     The ingredient
		 */
		@SuppressWarnings("SameParameterValue")
		private static void addStandardConversionRecipes(
				final PotionBrewing.Builder builder,
				final RegistryObject<Potion> standardPotion,
				final RegistryObject<Potion> longPotion,
				final RegistryObject<Potion> strongPotion,
				final RegistryObject<? extends Item> ingredient
		) {
			addStandardConversionRecipes(
					builder,
					standardPotion.getHolder().orElseThrow(),
					longPotion.getHolder().orElseThrow(),
					strongPotion.getHolder().orElseThrow(),
					ingredient.get()
			);
		}
	}

	public static class Ingredients {
		private static final DeferredRegister<IIngredientSerializer<?>> INGREDIENT_SERIALIZERS = DeferredRegister.create(ForgeRegistries.INGREDIENT_SERIALIZERS, TestMod3.MODID);

		private static boolean isInitialised;

		public static final RegistryObject<IIngredientSerializer<Ingredient>> CONDITIONAL = INGREDIENT_SERIALIZERS.register("conditional",
				() -> ConditionalIngredient.SERIALIZER
		);

		public static final RegistryObject<IIngredientSerializer<FluidContainerIngredient>> FLUID_CONTAINER = INGREDIENT_SERIALIZERS.register("fluid_container",
				FluidContainerIngredient.Serializer::new
		);

		public static final RegistryObject<MobSpawnerIngredient.Serializer> MOB_SPAWNER = INGREDIENT_SERIALIZERS.register("mob_spawner",
				() -> MobSpawnerIngredient.SERIALIZER
		);

		public static final RegistryObject<NeverIngredient.Serializer> NEVER = INGREDIENT_SERIALIZERS.register("never",
				NeverIngredient.Serializer::new
		);

		/**
		 * Registers the {@link DeferredRegister} instance with the mod event bus.
		 * <p>
		 * This should be called during mod construction.
		 *
		 * @param modEventBus The mod event bus
		 */
		public static void initialise(final IEventBus modEventBus) {
			if (isInitialised) {
				throw new IllegalStateException("Already initialised");
			}

			INGREDIENT_SERIALIZERS.register(modEventBus);

			isInitialised = true;
		}
	}

	public static class Recipes {
		private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TestMod3.MODID);

		private static boolean isInitialised;

		public static final RegistryObject<EnhancedShapedRecipe.Serializer> ENHANCED_SHAPED = RECIPE_SERIALIZERS.register("enhanced_shaped",
				EnhancedShapedRecipe.Serializer::new
		);

		public static final RegistryObject<ShapedArmourUpgradeRecipe.Serializer> ARMOUR_UPGRADE_SHAPED = RECIPE_SERIALIZERS.register("armour_upgrade_shaped",
				ShapedArmourUpgradeRecipe.Serializer::new
		);

		public static final RegistryObject<ShapelessCuttingRecipe.Serializer> CUTTING_SHAPELESS = RECIPE_SERIALIZERS.register("cutting_shapeless",
				ShapelessCuttingRecipe.Serializer::new
		);

		public static final RegistryObject<ShapelessFluidContainerRecipe.Serializer> FLUID_CONTAINER_SHAPELESS = RECIPE_SERIALIZERS.register("fluid_container_shapeless",
				ShapelessFluidContainerRecipe.Serializer::new
		);

		/**
		 * Registers the {@link DeferredRegister} instance with the mod event bus.
		 * <p>
		 * This should be called during mod construction.
		 *
		 * @param modEventBus The mod event bus
		 */
		public static void initialise(final IEventBus modEventBus) {
			if (isInitialised) {
				throw new IllegalStateException("Already initialised");
			}

			RECIPE_SERIALIZERS.register(modEventBus);

			isInitialised = true;
		}
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class RecipeRemover extends SimplePreparableReloadListener<Unit> {
		private static final Logger LOGGER = LogUtils.getLogger();

		private static final Method APPLY = ObfuscationReflectionHelper.findMethod(
				RecipeManager.class,
				"apply",
				RecipeMap.class,
				ResourceManager.class,
				ProfilerFiller.class
		);

		private final RecipeManager recipeManager;
		private final RegistryAccess registryAccess;

		private final CraftingInput craftingInput = CraftingInput.of(
				3,
				3,
				NonNullList.withSize(9, Items.BARRIER.getDefaultInstance())
		);

		public RecipeRemover(final RecipeManager recipeManager, final RegistryAccess registryAccess) {
			this.recipeManager = recipeManager;
			this.registryAccess = registryAccess;
		}

		/**
		 * Adds this listener to the ResourceManager's list.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void addReloadListener(final AddReloadListenerEvent event) {
			event.addListener(
					new RecipeRemover(event.getServerResources().getRecipeManager(), event.getRegistryAccess())
			);
		}

		@Override
		protected Unit prepare(final ResourceManager resourceManager, final ProfilerFiller profilerFiller) {
			return Unit.INSTANCE;
		}

		/**
		 * Removes recipes from the recipe manager after it's reloaded.
		 */
		@Override
		protected void apply(final Unit unit, final ResourceManager resourceManager, final ProfilerFiller profilerFiller) {
			final var recipes = new ArrayList<>(recipeManager.getRecipes());

			removeRecipes(recipes, FireworkRocketRecipe.class);
			removeRecipes(recipes, FireworkStarRecipe.class);
			removeRecipes(recipes, FireworkStarFadeRecipe.class);
			// TODO: This fires before tags are bound, causing recipes with tag ingredients to fail
			/*
			removeRecipes(recipes, ModTags.Items.VANILLA_DYES);
			removeRecipes(recipes, ModTags.Items.VANILLA_TERRACOTTA);
			*/

			final var recipeMap = RecipeMap.create(recipes);

			try {
				APPLY.invoke(recipeManager, recipeMap, resourceManager, profilerFiller);
			} catch (final IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException("Failed to replace recipes", e);
			}
		}

		/**
		 * Removes all crafting recipes with an output item contained in the specified tag.
		 *
		 * @param recipes The recipe list
		 * @param tag     The tag
		 */
		private void removeRecipes(final Collection<RecipeHolder<?>> recipes, final TagKey<Item> tag) {
			final var recipesRemoved = removeRecipes(recipes, recipe -> {
				final var resultItem = switch (recipe) {
					case final CraftingRecipe craftingRecipe -> craftingRecipe.assemble(craftingInput, registryAccess);

					case final SingleItemRecipe singleItemRecipe ->
							singleItemRecipe.assemble(new SingleRecipeInput(ItemStack.EMPTY), registryAccess);

					case final SmithingRecipe smithingRecipe -> smithingRecipe.assemble(
							new SmithingRecipeInput(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY),
							registryAccess
					);

					case null, default -> ItemStack.EMPTY;
				};

				return !resultItem.isEmpty() && resultItem.is(tag);
			});

			LOGGER.info("Removed {} recipe(s) for tag {}", recipesRemoved, tag.location());
		}

		/**
		 * Remove all crafting recipes that are instances of the specified class.
		 * <p>
		 * Test for this thread:
		 * https://www.minecraftforge.net/forum/topic/33420-removing-vanilla-recipes/
		 *
		 * @param recipes     The recipe list
		 * @param recipeClass The recipe class
		 */
		private void removeRecipes(final Collection<RecipeHolder<?>> recipes, final Class<? extends Recipe<?>> recipeClass) {
			final var recipesRemoved = removeRecipes(recipes, recipeClass::isInstance);

			LOGGER.info("Removed {} recipe(s) for class {}", recipesRemoved, recipeClass);
		}

		/**
		 * Remove all crafting recipes that match the specified predicate.
		 *
		 * @param recipes   The recipe list
		 * @param predicate The predicate
		 * @return The number of recipes removed
		 */
		private int removeRecipes(final Collection<RecipeHolder<?>> recipes, final Predicate<Recipe<?>> predicate) {
			// Get the list of recipes to remove
			final var recipesToRemove = recipes.stream()
					.filter(holder -> predicate.test(holder.value()))
					.toList();

			// Remove the recipes from the list
			recipes.removeAll(recipesToRemove);

			return recipesToRemove.size();
		}
	}
}
