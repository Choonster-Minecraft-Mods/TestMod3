package choonster.testmod3.data.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

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

	@Override
	public ResourceLocation id() {
		return baseRecipe.id();
	}

	@Override
	public RecipeSerializer<?> type() {
		return baseRecipe.type();
	}

	@Nullable
	@Override
	public AdvancementHolder advancement() {
		return baseRecipe.advancement();
	}
}
