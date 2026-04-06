package choonster.testmod3.world.item.crafting.recipe;


import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
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
	final CommonInfo commonInfo;
	final CraftingBookInfo bookInfo;
	final ItemStackTemplate result;
	final List<Ingredient> ingredients;

	private final ShapelessRecipe innerRecipe;

	protected BaseShapelessRecipe(
			final CommonInfo commonInfo,
			final CraftingBookInfo bookInfo,
			final ItemStackTemplate result,
			final List<Ingredient> ingredients
	) {
		this.commonInfo = commonInfo;
		this.bookInfo = bookInfo;
		this.result = result;
		this.ingredients = ingredients;

		innerRecipe = new ShapelessRecipe(commonInfo, bookInfo, result, ingredients);
	}

	@Override
	public boolean showNotification() {
		return innerRecipe.showNotification();
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
	public ItemStack assemble(final CraftingInput input) {
		return innerRecipe.assemble(input);
	}

	@Override
	public List<RecipeDisplay> display() {
		return innerRecipe.display();
	}
}
