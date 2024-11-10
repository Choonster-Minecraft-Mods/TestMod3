package choonster.testmod3.world.item.crafting.recipe;


import choonster.testmod3.init.ModCrafting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.List;

/**
 * A {@link ShapelessRecipe} that allows the result to have NBT.
 *
 * @author Choonster
 */
public class EnhancedShapelessRecipe extends ShapelessRecipe {
	private EnhancedShapelessRecipe(
			final String group,
			final CraftingBookCategory category,
			final ItemStack result,
			final List<Ingredient> ingredients
	) {
		super(group, category, result, ingredients);
	}

	@Override
	public RecipeSerializer<ShapelessRecipe> getSerializer() {
		return ModCrafting.Recipes.ENHANCED_SHAPELESS.get();
	}

	public static class Serializer extends ShapelessRecipeSerializer<ShapelessRecipe> {
		public Serializer() {
			super(EnhancedShapelessRecipe::new);
		}
	}
}
