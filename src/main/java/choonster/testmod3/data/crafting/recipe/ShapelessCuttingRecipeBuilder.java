package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.world.item.crafting.recipe.ShapelessCuttingRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

/**
 * Builder for {@link ShapelessCuttingRecipe}.
 *
 * @author Choonster
 */
public class ShapelessCuttingRecipeBuilder extends EnhancedShapelessRecipeBuilder<ShapelessCuttingRecipe, ShapelessCuttingRecipeBuilder> {
	protected ShapelessCuttingRecipeBuilder(final RecipeCategory category, final ItemStack result) {
		super(category, result, ModCrafting.Recipes.CUTTING_SHAPELESS.get());
	}

	/**
	 * Creates a new builder for a shapeless cutting recipe.
	 *
	 * @param result The recipe result item
	 * @return The builder
	 */
	public static ShapelessCuttingRecipeBuilder shapelessCuttingRecipe(final RecipeCategory category, final ItemLike result) {
		return shapelessCuttingRecipe(category, new ItemStack(result));
	}

	/**
	 * Creates a new builder for a shapeless cutting recipe.
	 *
	 * @param result The recipe result item
	 * @param count  The recipe result count
	 * @return The builder
	 */
	public static ShapelessCuttingRecipeBuilder shapelessCuttingRecipe(final RecipeCategory category, final ItemLike result, final int count) {
		return shapelessCuttingRecipe(category, new ItemStack(result, count));
	}

	/**
	 * Creates a new builder for a shapeless cutting recipe.
	 *
	 * @param result The recipe result
	 * @return The builder
	 */
	public static ShapelessCuttingRecipeBuilder shapelessCuttingRecipe(final RecipeCategory category, final ItemStack result) {
		return new ShapelessCuttingRecipeBuilder(category, result);
	}
}
