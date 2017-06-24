package choonster.testmod3.init;

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
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Adds and removes recipes.
 */
public class ModRecipes {
	/**
	 * Add this mod's recipes.
	 */
	public static void registerRecipes() {
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

	/**
	 * Remove crafting recipes.
	 */
	public static void removeCraftingRecipes() {
		removeRecipeClass(RecipeFireworks.class);
		removeRecipe(Items.DYE);
		removeRecipe(Blocks.STAINED_HARDENED_CLAY);
	}

	/**
	 * Remove all crafting recipes with the specified {@link Block} as their output.
	 *
	 * @param output The output Block
	 */
	private static void removeRecipe(final Block output) {
		final Item item = Item.getItemFromBlock(output);
		assert item != Items.AIR;

		removeRecipe(item);
	}

	/**
	 * Remove all crafting recipes with the specified {@link Item} as their output.
	 * <p>
	 * Adapted from Rohzek's code in this post:
	 * http://www.minecraftforge.net/forum/index.php/topic,33631.0.html
	 *
	 * @param output The output Item
	 */
	private static void removeRecipe(final Item output) {
		/* FIXME: Can recipes still be removed?
		int recipesRemoved = 0;

		final List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		final Iterator<IRecipe> remover = recipes.iterator();

		while (remover.hasNext()) {
			final ItemStack itemstack = remover.next().getRecipeOutput();

			// If the recipe's output Item is the specified Item,
			if (!itemstack.isEmpty() && itemstack.getItem() == output) {
				// Remove the recipe
				remover.remove();
				recipesRemoved++;
			}
		}

		Logger.info("Removed %d recipes for %s", recipesRemoved, output.getRegistryName());
		*/
	}

	/**
	 * Remove all crafting recipes that are instances of the specified class.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforge.net/forum/index.php/topic,33631.0.html
	 *
	 * @param recipeClass The recipe class
	 */
	private static void removeRecipeClass(final Class<? extends IRecipe> recipeClass) {
		/* FIXME: Can recipes still be removed?
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
		*/
	}
}
