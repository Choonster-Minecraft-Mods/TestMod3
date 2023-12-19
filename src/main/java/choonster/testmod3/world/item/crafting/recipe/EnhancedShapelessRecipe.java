package choonster.testmod3.world.item.crafting.recipe;


import choonster.testmod3.init.ModCrafting;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

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
			final NonNullList<Ingredient> ingredients
	) {
		super(group, category, result, ingredients);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModCrafting.Recipes.ENHANCED_SHAPELESS.get();
	}

	public static class Serializer extends ShapelessRecipeSerializer<EnhancedShapelessRecipe> {
		public Serializer() {
			super(EnhancedShapelessRecipe::new);
		}
	}
}
