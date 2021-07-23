package choonster.testmod3.data.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * An {@link IFinishedRecipe} that delegates to another {@link IFinishedRecipe} instance.
 *
 * @author Choonster
 */
public class DelegateFinishedRecipe implements IFinishedRecipe {
	protected final IFinishedRecipe baseRecipe;

	public DelegateFinishedRecipe(final IFinishedRecipe baseRecipe) {
		this.baseRecipe = baseRecipe;
	}

	@Override
	public void serializeRecipeData(final JsonObject json) {
		baseRecipe.serializeRecipeData(json);
	}

	/**
	 * Gets the ID for the recipe.
	 */
	@Override
	public ResourceLocation getId() {
		return baseRecipe.getId();
	}

	@Override
	public IRecipeSerializer<?> getType() {
		return baseRecipe.getType();
	}

	/**
	 * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
	 */
	@Override
	@Nullable
	public JsonObject serializeAdvancement() {
		return baseRecipe.serializeAdvancement();
	}

	/**
	 * Gets the ID for the advancement associated with this recipe. Should not be null if {@link #getAdvancementJson} is
	 * non-null.
	 */
	@Override
	@Nullable
	public ResourceLocation getAdvancementId() {
		return baseRecipe.getAdvancementId();
	}
}
