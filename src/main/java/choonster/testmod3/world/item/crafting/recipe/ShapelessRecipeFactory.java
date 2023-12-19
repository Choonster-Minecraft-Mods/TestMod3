package choonster.testmod3.world.item.crafting.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;

/**
 * Represents a factory or constructor for {@link ShapelessRecipe} classes.
 *
 * @author Choonster
 */
public interface ShapelessRecipeFactory<T extends ShapelessRecipe> {
	T createRecipe(
			String group,
			CraftingBookCategory category,
			ItemStack result,
			NonNullList<Ingredient> ingredients
	);
}
