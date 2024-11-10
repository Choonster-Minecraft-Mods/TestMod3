package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.world.item.crafting.recipe.ShapedArmourUpgradeRecipe;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

/**
 * Builder for {@link ShapedArmourUpgradeRecipe}.
 *
 * @author Choonster
 */
public class ShapedArmourUpgradeRecipeBuilder extends EnhancedShapedRecipeBuilder<ShapedArmourUpgradeRecipe, ShapedArmourUpgradeRecipeBuilder> {
	protected ShapedArmourUpgradeRecipeBuilder(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemStack result
	) {
		super(items, category, result, ModCrafting.Recipes.ARMOUR_UPGRADE_SHAPED.get());
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
		return shapedArmourUpgradeRecipe(items, category, new ItemStack(result));
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
			final ItemStack result
	) {
		return new ShapedArmourUpgradeRecipeBuilder(items, category, result);
	}

	/**
	 * Validates that the recipe result is damageable.
	 *
	 * @param key The recipe ID
	 */
	@Override
	protected void ensureValid(final ResourceKey<Recipe<?>> key) {
		super.ensureValid(key);

		if (!result.isDamageableItem()) {
			throw new IllegalStateException("Shaped Armour Upgrade Recipe " + key + " must have damageable result");
		}
	}
}
