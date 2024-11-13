package choonster.testmod3.world.item.crafting.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

/**
 * Represents a factory or constructor for {@link BaseShapelessRecipe} classes.
 *
 * @author Choonster
 */
public interface ShapelessRecipeFactory<T extends BaseShapelessRecipe> {
	T createRecipe(
			String group,
			CraftingBookCategory category,
			ItemStack result,
			List<Ingredient> ingredients
	);
}
