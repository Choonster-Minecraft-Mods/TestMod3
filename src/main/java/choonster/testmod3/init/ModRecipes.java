package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.crafting.recipe.DummyRecipe;
import choonster.testmod3.util.RegistryUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeFireworks;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Adds and removes recipes.
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModRecipes {
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Add this mod's recipes.
	 */
	@SubscribeEvent
	public static void registerRecipes(final FMLCommonSetupEvent event) {
		addSmeltingRecipes();
		addBrewingRecipes();
	}

	/**
	 * Add this mod's smelting recipes.
	 */
	private static void addSmeltingRecipes() {
		GameRegistry.addSmelting(ModItems.SUBSCRIPTS, new ItemStack(ModItems.DIMENSION_REPLACEMENT), 0.35f);
	}

	/**
	 * Add this mod's brewing recipes.
	 */
	private static void addBrewingRecipes() {
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
		PotionHelper.addMix(PotionTypes.AWKWARD, ingredient, standardPotionType);
		PotionHelper.addMix(standardPotionType, Items.REDSTONE, longPotionType);
		PotionHelper.addMix(standardPotionType, Items.GLOWSTONE_DUST, strongPotionType);
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class RegistrationHandler {
		/**
		 * Register this mod's Ore Dictionary entries.
		 *
		 * @param event The event
		 */
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void registerOreDictEntries(final RegistryEvent.Register<Item> event) {
			OreDictionary.registerOre("plankWood", ModBlocks.PLANKS);

			final IForgeRegistry<Item> itemRegistry = ForgeRegistries.ITEMS;

			// Test for this thread: http://www.minecraftforge.net/forum/topic/59744-112-how-to-disable-some-mod-recipe-files-via-config-file/
			// Can't use the fields from ModItems because object holders haven't been applied for Items yet
			OreDictionary.registerOre("itemRubber", RegistryUtil.getRegistryEntry(itemRegistry, "rubber"));
		}

		/**
		 * Remove crafting recipes.
		 *
		 * @param event The event
		 */
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void removeRecipes(final RegistryEvent.Register<IRecipe> event) {
			removeRecipes(RecipeFireworks.class);
			removeRecipes(Items.DYE);
			removeRecipes(Blocks.STAINED_HARDENED_CLAY);
		}

		/**
		 * Remove all crafting recipes with the specified {@link Block} as their output.
		 *
		 * @param output The output Block
		 */
		private static void removeRecipes(final Block output) {
			removeRecipes(Item.getItemFromBlock(output));
		}

		/**
		 * Remove all crafting recipes with the specified {@link Item} as their output.
		 *
		 * @param output The output Item
		 */
		private static void removeRecipes(final Item output) {
			final int recipesRemoved = removeRecipes(recipe -> {
				final ItemStack recipeOutput = recipe.getRecipeOutput();
				return !recipeOutput.isEmpty() && recipeOutput.getItem() == output;
			});

			LOGGER.info("Removed {} recipe(s) for {}", recipesRemoved, output.getRegistryName());
		}

		/**
		 * Remove all crafting recipes that are instances of the specified class.
		 * <p>
		 * Test for this thread:
		 * http://www.minecraftforge.net/forum/index.php/topic,33631.0.html
		 *
		 * @param recipeClass The recipe class
		 */
		private static void removeRecipes(final Class<? extends IRecipe> recipeClass) {
			final int recipesRemoved = removeRecipes(recipeClass::isInstance);

			LOGGER.info("Removed {} recipe(s) for {}", recipesRemoved, recipeClass);
		}

		/**
		 * Remove all crafting recipes that match the specified predicate.
		 *
		 * @param predicate The predicate
		 * @return The number of recipes removed
		 */
		private static int removeRecipes(final Predicate<IRecipe> predicate) {
			int recipesRemoved = 0;

			final IForgeRegistry<IRecipe> registry = ForgeRegistries.RECIPES;
			final List<IRecipe> toRemove = new ArrayList<>();

			for (final IRecipe recipe : registry) {
				if (predicate.test(recipe)) {
					toRemove.add(recipe);
					recipesRemoved++;
				}
			}

			LOGGER.info("Overriding recipes with dummy recipes, please ignore the following \"Dangerous alternative prefix\" warnings.");
			toRemove.forEach(recipe -> {
				final ResourceLocation registryName = Objects.requireNonNull(recipe.getRegistryName());
				final IRecipe replacement = new DummyRecipe().setRegistryName(registryName);
				registry.register(replacement);
			});

			return recipesRemoved;
		}
	}
}
