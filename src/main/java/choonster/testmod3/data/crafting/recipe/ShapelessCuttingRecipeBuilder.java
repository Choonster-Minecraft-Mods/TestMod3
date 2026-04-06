package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.world.item.crafting.recipe.ShapelessCuttingRecipe;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;

/**
 * Builder for {@link ShapelessCuttingRecipe}.
 *
 * @author Choonster
 */
public class ShapelessCuttingRecipeBuilder extends BaseShapelessRecipeBuilder<ShapelessCuttingRecipe, ShapelessCuttingRecipeBuilder> {
	protected ShapelessCuttingRecipeBuilder(final HolderGetter<Item> items, final RecipeCategory category, final ItemStackTemplate result) {
		super(items, category, result, ShapelessCuttingRecipe::new);
	}

	/**
	 * Creates a new builder for a shapeless cutting recipe.
	 *
	 * @param items  The item registry
	 * @param result The recipe result item
	 * @return The builder
	 */
	public static ShapelessCuttingRecipeBuilder shapelessCuttingRecipe(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemLike result
	) {
		return shapelessCuttingRecipe(items, category, new ItemStackTemplate(result.asItem()));
	}

	/**
	 * Creates a new builder for a shapeless cutting recipe.
	 *
	 * @param items  The item registry
	 * @param result The recipe result item
	 * @param count  The recipe result count
	 * @return The builder
	 */
	public static ShapelessCuttingRecipeBuilder shapelessCuttingRecipe(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemLike result,
			final int count
	) {
		return shapelessCuttingRecipe(items, category, new ItemStackTemplate(result.asItem(), count));
	}

	/**
	 * Creates a new builder for a shapeless cutting recipe.
	 *
	 * @param items  The item registry
	 * @param result The recipe result
	 * @return The builder
	 */
	public static ShapelessCuttingRecipeBuilder shapelessCuttingRecipe(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemStackTemplate result
	) {
		return new ShapelessCuttingRecipeBuilder(items, category, result);
	}
}
