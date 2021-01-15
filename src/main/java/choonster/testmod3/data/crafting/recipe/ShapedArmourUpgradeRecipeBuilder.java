package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.crafting.recipe.ShapedArmourUpgradeRecipe;
import choonster.testmod3.init.ModCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

/**
 * Builder for {@link ShapedArmourUpgradeRecipe}.
 *
 * @author Choonster
 */
public class ShapedArmourUpgradeRecipeBuilder extends EnhancedShapedRecipeBuilder<ShapedArmourUpgradeRecipe, ShapedArmourUpgradeRecipeBuilder> {
	protected ShapedArmourUpgradeRecipeBuilder(final ItemStack result) {
		super(result, ModCrafting.Recipes.ARMOUR_UPGRADE_SHAPED.get());
	}

	/**
	 * Creates a new builder for a shaped armour upgrade recipe.
	 *
	 * @param result The recipe result item
	 * @return The builder
	 */
	public static ShapedArmourUpgradeRecipeBuilder shapedArmourUpgradeRecipe(final IItemProvider result) {
		return shapedArmourUpgradeRecipe(new ItemStack(result));
	}

	/**
	 * Creates a new builder for a shaped armour upgrade recipe.
	 *
	 * @param result The recipe result
	 * @return The builder
	 */
	public static ShapedArmourUpgradeRecipeBuilder shapedArmourUpgradeRecipe(final ItemStack result) {
		return new ShapedArmourUpgradeRecipeBuilder(result);
	}

	/**
	 * Validates that the recipe result is damageable.
	 *
	 * @param id The recipe ID
	 */
	@Override
	protected void validate(final ResourceLocation id) {
		super.validate(id);

		if (!result.isDamageable()) {
			throw new IllegalStateException("Shaped Armour Upgrade Recipe " + id + " must have damageable result");
		}
	}
}
