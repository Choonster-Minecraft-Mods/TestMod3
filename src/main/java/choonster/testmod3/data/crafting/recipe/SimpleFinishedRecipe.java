package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.util.ModJsonUtil;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link FinishedRecipe} that allows the recipe result to have NBT.
 * Delegates all other logic to another {@link FinishedRecipe} instance.
 *
 * @author Choonster
 */
public class SimpleFinishedRecipe extends DelegateFinishedRecipe {
	private final RecipeSerializer<?> serializer;
	@Nullable
	private final CompoundTag resultNBT;

	public SimpleFinishedRecipe(final FinishedRecipe baseRecipe, final ItemStack result, final RecipeSerializer<?> serializer) {
		super(baseRecipe);
		this.serializer = serializer;
		resultNBT = result.getTag();
	}

	@Override
	public void serializeRecipeData(final JsonObject json) {
		super.serializeRecipeData(json);

		if (resultNBT != null) {
			json.add("nbt", ModJsonUtil.toJson(CompoundTag.CODEC, resultNBT));
		}
	}

	@Override
	public RecipeSerializer<?> type() {
		return serializer;
	}
}
