package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

/**
 * A {@link ShapedRecipe} that allows the result to have NBT.
 *
 * @author Choonster
 */
public class EnhancedShapedRecipe extends ShapedRecipe {
	public EnhancedShapedRecipe(
			final String group,
			final CraftingBookCategory category,
			final ShapedRecipePattern pattern,
			final ItemStack result,
			final boolean showNotification
	) {
		super(group, category, pattern, result, showNotification);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModCrafting.Recipes.ENHANCED_SHAPED.get();
	}

	public static class Serializer extends ShapedRecipeSerializer<EnhancedShapedRecipe> {
		public Serializer() {
			super(EnhancedShapedRecipe::new);
		}
	}
}
