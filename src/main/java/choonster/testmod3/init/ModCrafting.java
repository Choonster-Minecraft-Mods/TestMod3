package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.RegistryUtil;
import choonster.testmod3.world.item.crafting.ingredient.ConditionalIngredient;
import choonster.testmod3.world.item.crafting.ingredient.FluidContainerIngredient;
import choonster.testmod3.world.item.crafting.ingredient.MobSpawnerIngredient;
import choonster.testmod3.world.item.crafting.ingredient.NeverIngredient;
import choonster.testmod3.world.item.crafting.recipe.ShapedArmourUpgradeRecipe;
import choonster.testmod3.world.item.crafting.recipe.ShapelessCuttingRecipe;
import choonster.testmod3.world.item.crafting.recipe.ShapelessFluidContainerRecipe;
import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Manages this mod's recipes and ingredients and removes recipes.
 */
public class ModCrafting {

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class Brewing {
		private static final Logger LOGGER = LogUtils.getLogger();

		private static final Method ADD_MIX = ObfuscationReflectionHelper.findMethod(PotionBrewing.class, /* addMix */ "m_43513_", Potion.class, Item.class, Potion.class);

		/**
		 * Add this mod's brewing recipes.
		 *
		 * @param event The common setup event
		 */
		@SubscribeEvent
		public static void registerBrewingRecipes(final FMLCommonSetupEvent event) {
			event.enqueueWork(() ->
					addStandardConversionRecipes(ModPotions.TEST.get(), ModPotions.LONG_TEST.get(), ModPotions.STRONG_TEST.get(), ModItems.ARROW.get())
			);
		}

		/**
		 * Add the standard conversion recipes for the specified {@link Potion}s:
		 * <ul>
		 * <li>Awkward + Ingredient = Standard</li>
		 * <li>Standard + Redstone = Long</li>
		 * <li>Standard + Glowstone = Strong</li>
		 * </ul>
		 *
		 * @param standardPotionType The standard PotionType
		 * @param longPotionType     The long PotionType
		 * @param strongPotionType   The strong PotionType
		 * @param ingredient         The ingredient
		 */
		private static void addStandardConversionRecipes(final Potion standardPotionType, final Potion longPotionType, final Potion strongPotionType, final Item ingredient) {
			try {
				ADD_MIX.invoke(null, Potions.AWKWARD, ingredient, standardPotionType);
				ADD_MIX.invoke(null, standardPotionType, Items.REDSTONE, longPotionType);
				ADD_MIX.invoke(null, standardPotionType, Items.GLOWSTONE_DUST, strongPotionType);
			} catch (final IllegalAccessException | InvocationTargetException e) {
				LOGGER.error("Failed to add potion recipes for potion type {}/ingredient item {}", RegistryUtil.getKey(standardPotionType), RegistryUtil.getKey(ingredient), e);
			}
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
	public static class RecipeRemover {
		private static final Logger LOGGER = LogUtils.getLogger();

		private static final Field RECIPES = ObfuscationReflectionHelper.findField(RecipeManager.class,  /* recipes */ "f_44007_");

		/**
		 * Removes recipes from the server's recipe manager when it starts up.
		 *
		 * @param event The server starting event
		 */
		@SubscribeEvent
		public static void removeRecipes(final ServerStartedEvent event) {
			final var recipeManager = event.getServer().getRecipeManager();
			final var registryAccess = event.getServer().registryAccess();

			removeRecipes(recipeManager, FireworkRocketRecipe.class);
			removeRecipes(recipeManager, FireworkStarRecipe.class);
			removeRecipes(recipeManager, FireworkStarFadeRecipe.class);
			removeRecipes(recipeManager, registryAccess, ModTags.Items.VANILLA_DYES);
			removeRecipes(recipeManager, registryAccess, ModTags.Items.VANILLA_TERRACOTTA);
		}

		/**
		 * Removes all crafting recipes with an output item contained in the specified tag.
		 *
		 * @param recipeManager  The recipe manager
		 * @param registryAccess The registry access
		 * @param tag            The tag
		 */
		private static void removeRecipes(final RecipeManager recipeManager, final RegistryAccess registryAccess, final TagKey<Item> tag) {
			final var recipesRemoved = removeRecipes(recipeManager, recipe -> {
				final var resultItem = recipe.getResultItem(registryAccess);
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
		 * @param recipeManager The recipe manager
		 * @param recipeClass   The recipe class
		 */
		private static void removeRecipes(final RecipeManager recipeManager, final Class<? extends Recipe<?>> recipeClass) {
			final var recipesRemoved = removeRecipes(recipeManager, recipeClass::isInstance);

			LOGGER.info("Removed {} recipe(s) for class {}", recipesRemoved, recipeClass);
		}

		/**
		 * Remove all crafting recipes that match the specified predicate.
		 *
		 * @param recipeManager The recipe manager
		 * @param predicate     The predicate
		 * @return The number of recipes removed
		 */
		private static int removeRecipes(final RecipeManager recipeManager, final Predicate<Recipe<?>> predicate) {
			final Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>> existingRecipes;
			try {
				@SuppressWarnings("unchecked") final var recipesMap = (Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>>) RECIPES.get(recipeManager);
				existingRecipes = recipesMap;
			} catch (final IllegalAccessException e) {
				throw new RuntimeException("Couldn't get recipes map while removing recipes", e);
			}

			final var removedCounts = new Object2IntOpenHashMap<RecipeType<?>>();
			final var newRecipes = ImmutableMap.<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>>builder();

			// For each recipe type, create a new map that doesn't contain the recipes to be removed
			existingRecipes.forEach((recipeType, existingRecipesForType) -> {
				final ImmutableMap<ResourceLocation, RecipeHolder<?>> newRecipesForType = existingRecipesForType.entrySet()
						.stream()
						.filter(entry -> !predicate.test(entry.getValue().value()))
						.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));

				removedCounts.put(recipeType, existingRecipesForType.size() - newRecipesForType.size());
				newRecipes.put(recipeType, newRecipesForType);
			});

			final var removedCount = removedCounts.values().intStream().reduce(0, Integer::sum);

			try {
				RECIPES.set(recipeManager, newRecipes.build());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException("Couldn't replace recipes map while removing recipes", e);
			}

			return removedCount;
		}
	}
}
