package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.crafting.ingredient.ConditionalIngredientSerializer;
import choonster.testmod3.crafting.ingredient.FilledUniversalBucketIngredientSerializer;
import choonster.testmod3.crafting.ingredient.IngredientNever;
import choonster.testmod3.crafting.ingredient.MobSpawnerIngredientSerializer;
import choonster.testmod3.crafting.recipe.ShapedArmourUpgradeRecipe;
import choonster.testmod3.crafting.recipe.ShapelessCuttingRecipe;
import choonster.testmod3.util.LogUtil;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.PotionType;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.common.extensions.IForgeRecipe;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Manages this mod's recipes and ingredients and removes recipes.
 */
public class ModCrafting {

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class Brewing {
		private static final Logger LOGGER = LogManager.getLogger();

		private static final Method ADD_MIX = ObfuscationReflectionHelper.findMethod(PotionBrewing.class, "func_193357_a" /* addMix */, PotionType.class, Item.class, PotionType.class);

		/**
		 * Add this mod's brewing recipes.
		 */
		@SubscribeEvent
		public static void registerBrewingRecipes(final FMLCommonSetupEvent event) {
			addStandardConversionRecipes(ModPotionTypes.TEST, ModPotionTypes.LONG_TEST, ModPotionTypes.STRONG_TEST, ModItems.ARROW);
		}

		/**
		 * Add the standard conversion recipes for the specified {@link PotionType}s:
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
		private static void addStandardConversionRecipes(final PotionType standardPotionType, final PotionType longPotionType, final PotionType strongPotionType, final Item ingredient) {
			try {
				ADD_MIX.invoke(null, PotionTypes.AWKWARD, ingredient, standardPotionType);
				ADD_MIX.invoke(null, standardPotionType, Items.REDSTONE, longPotionType);
				ADD_MIX.invoke(null, standardPotionType, Items.GLOWSTONE_DUST, strongPotionType);
			} catch (final IllegalAccessException | InvocationTargetException e) {
				LogUtil.error(LOGGER, e, "Failed to add potion recipes for potion type {}/ingredient item {}", standardPotionType.getRegistryName(), ingredient.getRegistryName());
			}
		}
	}

	@SuppressWarnings("unused")
	public static class Ingredients {
		public static IIngredientSerializer<Ingredient> CONDITIONAL = CraftingHelper.register(new ResourceLocation(TestMod3.MODID, "conditional"), new ConditionalIngredientSerializer());
		public static IIngredientSerializer<IngredientNBT> FILLED_UNIVERSAL_BUCKET = CraftingHelper.register(new ResourceLocation(TestMod3.MODID, "filled_universal_bucket"), new FilledUniversalBucketIngredientSerializer());
		public static IIngredientSerializer<IngredientNBT> MOB_SPAWNER = CraftingHelper.register(new ResourceLocation(TestMod3.MODID, "mob_spawner"), new MobSpawnerIngredientSerializer());
		public static IIngredientSerializer<IngredientNever> NEVER = CraftingHelper.register(new ResourceLocation(TestMod3.MODID, "never"), new IngredientNever.Serializer());

		public static void register() {
			// No-op method to ensure that this class is loaded and its static initialisers are run
		}
	}

	@SuppressWarnings("unused")
	public static class Recipes {
		public static IRecipeSerializer<ShapedArmourUpgradeRecipe> ARMOUR_UPGRADE_SHAPED = RecipeSerializers.register(new ShapedArmourUpgradeRecipe.Serializer());
		public static IRecipeSerializer<ShapelessCuttingRecipe> CUTTING_SHAPELESS = RecipeSerializers.register(new ShapelessCuttingRecipe.Serializer());

		public static void register() {
			// No-op method to ensure that this class is loaded and its static initialisers are run
		}
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class RecipeRemover {
		private static final Logger LOGGER = LogManager.getLogger();

		/**
		 * Removes recipes from the server's recipe manager when it starts up.
		 */
		@SubscribeEvent
		public static void removeRecipes(final FMLServerStartedEvent event) {
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
		private static void removeRecipes(final RecipeManager recipeManager, final Tag<Item> tag) {
			final int recipesRemoved = removeRecipes(recipeManager, recipe -> {
				final ItemStack recipeOutput = recipe.getRecipeOutput();
				return !recipeOutput.isEmpty() && recipeOutput.getItem().isIn(tag);
			});

			LOGGER.info("Removed {} recipe(s) for tag {}", recipesRemoved, tag.getId());
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
		private static void removeRecipes(final RecipeManager recipeManager, final Class<? extends IRecipe> recipeClass) {
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
		private static int removeRecipes(final RecipeManager recipeManager, final Predicate<IRecipe> predicate) {
			final Set<IRecipe> toRemove = recipeManager.getRecipes()
					.stream()
					.filter(predicate)
					.collect(Collectors.toSet());

			recipeManager.getRecipes().removeAll(toRemove);

			toRemove.stream()
					.map(IForgeRecipe::getType)
					.distinct()
					.forEach(recipeType -> {
						//noinspection SuspiciousMethodCalls
						recipeManager.getRecipes(recipeType).removeAll(toRemove);
					});

			return toRemove.size();
		}
	}
}
