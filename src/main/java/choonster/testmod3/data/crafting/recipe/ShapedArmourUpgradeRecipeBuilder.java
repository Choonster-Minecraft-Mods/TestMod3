package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.world.item.crafting.recipe.ShapedArmourUpgradeRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

/**
 * Builder for {@link ShapedArmourUpgradeRecipe}.
 *
 * @author Choonster
 */
public class ShapedArmourUpgradeRecipeBuilder extends EnhancedShapedRecipeBuilder<ShapedArmourUpgradeRecipe, ShapedArmourUpgradeRecipeBuilder> {
	protected ShapedArmourUpgradeRecipeBuilder(final RecipeCategory category, final ItemStack result) {
		super(category, result, ModCrafting.Recipes.ARMOUR_UPGRADE_SHAPED.get());
	}

	/**
	 * Creates a new builder for a shaped armour upgrade recipe.
	 *
	 * @param result The recipe result item
	 * @return The builder
	 */
	public static ShapedArmourUpgradeRecipeBuilder shapedArmourUpgradeRecipe(final RecipeCategory category, final ItemLike result) {
		return shapedArmourUpgradeRecipe(category, new ItemStack(result));
	}

	/**
	 * Creates a new builder for a shaped armour upgrade recipe.
	 *
	 * @param result The recipe result
	 * @return The builder
	 */
	public static ShapedArmourUpgradeRecipeBuilder shapedArmourUpgradeRecipe(final RecipeCategory category, final ItemStack result) {
		return new ShapedArmourUpgradeRecipeBuilder(category, result);
	}

	/**
	 * Validates that the recipe result is damageable.
	 *
	 * @param id The recipe ID
	 */
	@Override
	protected void ensureValid(final ResourceLocation id) {
		super.ensureValid(id);

		if (!result.isDamageableItem()) {
			throw new IllegalStateException("Shaped Armour Upgrade Recipe " + id + " must have damageable result");
		}
	}
}
