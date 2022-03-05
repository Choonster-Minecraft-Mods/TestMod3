package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.LogUtil;
import choonster.testmod3.world.item.crafting.ingredient.ConditionalIngredientSerializer;
import choonster.testmod3.world.item.crafting.ingredient.FluidContainerIngredient;
import choonster.testmod3.world.item.crafting.ingredient.IngredientNever;
import choonster.testmod3.world.item.crafting.ingredient.MobSpawnerIngredientSerializer;
import choonster.testmod3.world.item.crafting.recipe.ShapedArmourUpgradeRecipe;
import choonster.testmod3.world.item.crafting.recipe.ShapelessCuttingRecipe;
import choonster.testmod3.world.item.crafting.recipe.ShapelessFluidContainerRecipe;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.NBTIngredient;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		private static final Logger LOGGER = LogManager.getLogger();

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
				LogUtil.error(LOGGER, e, "Failed to add potion recipes for potion type {}/ingredient item {}", standardPotionType.getRegistryName(), ingredient.getRegistryName());
			}
		}
	}

	public static class Ingredients {
		public static final IIngredientSerializer<Ingredient> CONDITIONAL = CraftingHelper.register(new ResourceLocation(TestMod3.MODID, "conditional"), new ConditionalIngredientSerializer());
		public static final IIngredientSerializer<FluidContainerIngredient> FLUID_CONTAINER = CraftingHelper.register(new ResourceLocation(TestMod3.MODID, "fluid_container"), new FluidContainerIngredient.Serializer());
		public static final IIngredientSerializer<NBTIngredient> MOB_SPAWNER = CraftingHelper.register(new ResourceLocation(TestMod3.MODID, "mob_spawner"), new MobSpawnerIngredientSerializer());
		public static final IIngredientSerializer<IngredientNever> NEVER = CraftingHelper.register(new ResourceLocation(TestMod3.MODID, "never"), new IngredientNever.Serializer());

		public static void register() {
			// No-op method to ensure that this class is loaded and its static initialisers are run
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
		private static final Logger LOGGER = LogManager.getLogger();

		private static final Field RECIPES = ObfuscationReflectionHelper.findField(RecipeManager.class,  /* recipes */ "f_44007_");

		/**
		 * Removes recipes from the server's recipe manager when it starts up.
		 *
		 * @param event The server starting event
		 */
		@SubscribeEvent
		public static void removeRecipes(final ServerStartedEvent event) {
			final RecipeManager recipeManager = event.getServer().getRecipeManager();
			removeRecipes(recipeManager, FireworkRocketRecipe.class);
			removeRecipes(recipeManager, FireworkStarRecipe.class);
			removeRecipes(recipeManager, FireworkStarFadeRecipe.class);
			removeRecipes(recipeManager, ModTags.Items.VANILLA_DYES);
			removeRecipes(recipeManager, ModTags.Items.VANILLA_TERRACOTTA);
		}

		/**
		 * Removes all crafting recipes with an output item contained in the specified tag.
		 *
		 * @param recipeManager The recipe manager
		 * @param tag           The tag
		 */
		private static void removeRecipes(final RecipeManager recipeManager, final TagKey<Item> tag) {
			final int recipesRemoved = removeRecipes(recipeManager, recipe -> {
				final ItemStack resultItem = recipe.getResultItem();
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
			final int recipesRemoved = removeRecipes(recipeManager, recipeClass::isInstance);

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
			final Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> existingRecipes;
			try {
				@SuppressWarnings("unchecked")
				final Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipesMap = (Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>>) RECIPES.get(recipeManager);
				existingRecipes = recipesMap;
			} catch (final IllegalAccessException e) {
				throw new RuntimeException("Couldn't get recipes map while removing recipes", e);
			}

			final Object2IntMap<RecipeType<?>> removedCounts = new Object2IntOpenHashMap<>();
			final ImmutableMap.Builder<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> newRecipes = ImmutableMap.builder();

			// For each recipe type, create a new map that doesn't contain the recipes to be removed
			existingRecipes.forEach((recipeType, existingRecipesForType) -> {
				final ImmutableMap<ResourceLocation, Recipe<?>> newRecipesForType = existingRecipesForType.entrySet()
						.stream()
						.filter(entry -> !predicate.test(entry.getValue()))
						.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));

				removedCounts.put(recipeType, existingRecipesForType.size() - newRecipesForType.size());
				newRecipes.put(recipeType, newRecipesForType);
			});

			final int removedCount = removedCounts.values().intStream().reduce(0, Integer::sum);

			try {
				RECIPES.set(recipeManager, newRecipes.build());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException("Couldn't replace recipes map while removing recipes", e);
			}

			return removedCount;
		}
	}
}
