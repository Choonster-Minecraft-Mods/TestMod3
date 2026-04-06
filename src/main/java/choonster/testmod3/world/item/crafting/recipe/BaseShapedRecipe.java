package choonster.testmod3.world.item.crafting.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.IShapedRecipe;

import java.util.List;

/**
 * An extensible wrapper of {@link net.minecraft.world.item.crafting.ShapedRecipe}.
 *
 * @author Choonster
 */
public abstract class BaseShapedRecipe implements CraftingRecipe, IShapedRecipe<CraftingInput> {
	final CommonInfo commonInfo;
	final CraftingRecipe.CraftingBookInfo bookInfo;
	final ShapedRecipePattern pattern;
	final ItemStackTemplate result;
	final ShapedRecipe innerRecipe;

	protected BaseShapedRecipe(
			final CommonInfo commonInfo,
			final CraftingRecipe.CraftingBookInfo bookInfo,
			final ShapedRecipePattern pattern,
			final ItemStackTemplate result

	) {
		this.commonInfo = commonInfo;
		this.bookInfo = bookInfo;
		this.pattern = pattern;
		this.result = result;

		innerRecipe = new ShapedRecipe(commonInfo, bookInfo, pattern, result);
	}

	@Override
	public List<RecipeDisplay> display() {
		return innerRecipe.display();
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
	public int getRecipeWidth() {
		return innerRecipe.getRecipeWidth();
	}

	@Override
	public int getRecipeHeight() {
		return innerRecipe.getRecipeHeight();
	}
}
