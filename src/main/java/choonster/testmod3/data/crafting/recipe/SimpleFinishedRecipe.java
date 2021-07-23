package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.util.ModJsonUtil;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;

/**
 * An {@link IFinishedRecipe} that allows the recipe result to have NBT.
 * Delegates all other logic to another {@link IFinishedRecipe} instance.
 *
 * @author Choonster
 */
public class SimpleFinishedRecipe extends DelegateFinishedRecipe {
	private final IRecipeSerializer<?> serializer;
	private final CompoundNBT resultNBT;

	public SimpleFinishedRecipe(final IFinishedRecipe baseRecipe, final ItemStack result, final IRecipeSerializer<?> serializer) {
		super(baseRecipe);
		this.serializer = serializer;
		resultNBT = result.getTag();
	}

	@Override
	public void serializeRecipeData(final JsonObject json) {
		super.serializeRecipeData(json);

		if (resultNBT != null) {
			ModJsonUtil.setCompoundTag(json.getAsJsonObject("result"), "nbt", resultNBT);
		}
	}

	@Override
	public IRecipeSerializer<?> getType() {
		return serializer;
	}
}
