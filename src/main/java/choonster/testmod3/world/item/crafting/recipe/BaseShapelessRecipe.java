package choonster.testmod3.world.item.crafting.recipe;


import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * An extensible wrapper of {@link ShapelessRecipe}.
 *
 * @author Choonster
 */
public abstract class BaseShapelessRecipe implements CraftingRecipe {
	final ItemStack result;
	final List<Ingredient> ingredients;

	private final ShapelessRecipe innerRecipe;

	protected BaseShapelessRecipe(
			final String group,
			final CraftingBookCategory category,
			final ItemStack result,
			final List<Ingredient> ingredients
	) {
		this.result = result;
		this.ingredients = ingredients;

		innerRecipe = new ShapelessRecipe(group, category, result, ingredients);
	}

	@Override
	public String group() {
		return innerRecipe.group();
	}

	@Override
	public CraftingBookCategory category() {
		return innerRecipe.category();
	}

	@Override
	public PlacementInfo placementInfo() {
		return innerRecipe.placementInfo();
	}

	@Override
	public boolean matches(final CraftingInput input, final Level level) {
		return innerRecipe.matches(input, level);
	}

	@Override
	public ItemStack assemble(final CraftingInput input, final HolderLookup.Provider registries) {
		return innerRecipe.assemble(input, registries);
	}

	@Override
	public List<RecipeDisplay> display() {
		return innerRecipe.display();
	}
}
