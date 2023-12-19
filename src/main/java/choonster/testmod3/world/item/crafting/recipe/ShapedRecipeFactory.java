package choonster.testmod3.world.item.crafting.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

/**
 * Represents a factory or constructor for {@link ShapedRecipe} classes.
 *
 * @author Choonster
 */
public interface ShapedRecipeFactory<T extends ShapedRecipe> {
	T createRecipe(
			String group,
			CraftingBookCategory category,
			ShapedRecipePattern pattern,
			ItemStack result,
			boolean showNotification
	);
}
