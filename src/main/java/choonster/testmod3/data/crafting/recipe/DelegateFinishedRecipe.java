package choonster.testmod3.data.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;

/**
 * A {@link FinishedRecipe} that delegates to another {@link FinishedRecipe} instance.
 *
 * @author Choonster
 */
public class DelegateFinishedRecipe implements FinishedRecipe {
	protected final FinishedRecipe baseRecipe;

	public DelegateFinishedRecipe(final FinishedRecipe baseRecipe) {
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
	public RecipeSerializer<?> getType() {
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
	 * Gets the ID for the advancement associated with this recipe. Should not be null if {@link #serializeAdvancement()} is
	 * non-null.
	 */
	@Override
	@Nullable
	public ResourceLocation getAdvancementId() {
		return baseRecipe.getAdvancementId();
	}
}
