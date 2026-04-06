package choonster.testmod3.world.item.crafting.recipe;

import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Represents a factory or constructor for {@link BaseShapedRecipe}/{@link ShapedRecipe} classes.
 *
 * @author Choonster
 */
public interface ShapedRecipeFactory<T extends IShapedRecipe<?>> {
	T createRecipe(
			Recipe.CommonInfo commonInfo,
			CraftingRecipe.CraftingBookInfo bookInfo,
			ShapedRecipePattern pattern,
			ItemStackTemplate result
	);
}
