package choonster.testmod3.world.item.crafting.recipe;

import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

/**
 * Represents a factory or constructor for {@link BaseShapelessRecipe} classes.
 *
 * @author Choonster
 */
public interface ShapelessRecipeFactory<T extends BaseShapelessRecipe> {
	T createRecipe(
			Recipe.CommonInfo commonInfo,
			CraftingRecipe.CraftingBookInfo bookInfo,
			ItemStackTemplate result,
			List<Ingredient> ingredients
	);
}
