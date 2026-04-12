package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.world.item.crafting.recipe.ShapedArmourUpgradeRecipe;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;

/**
 * Builder for {@link ShapedArmourUpgradeRecipe}.
 *
 * @author Choonster
 */
public class ShapedArmourUpgradeRecipeBuilder extends BaseShapedRecipeBuilder<ShapedArmourUpgradeRecipe, ShapedArmourUpgradeRecipeBuilder> {
	protected ShapedArmourUpgradeRecipeBuilder(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemStackTemplate result
	) {
		super(items, category, result, ShapedArmourUpgradeRecipe::new);
	}

	/**
	 * Creates a new builder for a shaped armour upgrade recipe.
	 *
	 * @param items  The item registry
	 * @param result The recipe result item
	 * @return The builder
	 */
	public static ShapedArmourUpgradeRecipeBuilder shapedArmourUpgradeRecipe(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemLike result
	) {
		return shapedArmourUpgradeRecipe(items, category, new ItemStackTemplate(result.asItem()));
	}

	/**
	 * Creates a new builder for a shaped armour upgrade recipe.
	 *
	 * @param items  The item registry
	 * @param result The recipe result
	 * @return The builder
	 */
	public static ShapedArmourUpgradeRecipeBuilder shapedArmourUpgradeRecipe(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemStackTemplate result
	) {
		return new ShapedArmourUpgradeRecipeBuilder(items, category, result);
	}
}
