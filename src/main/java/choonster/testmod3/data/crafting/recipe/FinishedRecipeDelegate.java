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
public class FinishedRecipeDelegate implements IFinishedRecipe {
	protected final IFinishedRecipe baseRecipe;

	public FinishedRecipeDelegate(final IFinishedRecipe baseRecipe) {
		this.baseRecipe = baseRecipe;
	}

	@Override
	public void serialize(final JsonObject json) {
		baseRecipe.serialize(json);
	}

	/**
	 * Gets the ID for the recipe.
	 */
	@Override
	public ResourceLocation getID() {
		return baseRecipe.getID();
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return baseRecipe.getSerializer();
	}

	/**
	 * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
	 */
	@Override
	@Nullable
	public JsonObject getAdvancementJson() {
		return baseRecipe.getAdvancementJson();
	}

	/**
	 * Gets the ID for the advancement associated with this recipe. Should not be null if {@link #getAdvancementJson} is
	 * non-null.
	 */
	@Override
	@Nullable
	public ResourceLocation getAdvancementID() {
		return baseRecipe.getAdvancementID();
	}
}
