package choonster.testmod3.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Utility methods for {@link IRecipe}s.
 *
 * @author Choonster
 */
public class RecipeUtil {
	private static final Method KEY_FROM_JSON = ObfuscationReflectionHelper.findMethod(ShapedRecipe.class, /* keyFromJson */ "func_192408_a", JsonObject.class);
	private static final Method SHRINK = ObfuscationReflectionHelper.findMethod(ShapedRecipe.class, /* shrink */ "func_194134_a", String[].class);
	private static final Method PATTERN_FROM_JSON = ObfuscationReflectionHelper.findMethod(ShapedRecipe.class, /* patternFromJson */ "func_192407_a", JsonArray.class);
	private static final Method DISSOLVE_PATTERN = ObfuscationReflectionHelper.findMethod(ShapedRecipe.class, /* dissolvePattern */ "func_192402_a", String[].class, Map.class, int.class, int.class);

	/**
	 * Parse the ingredients of a shaped recipe.
	 * <p>
	 * Adapted from {@link ShapedRecipe.Serializer#fromJson(ResourceLocation, JsonObject)}.
	 *
	 * @param json The recipe's JSON object
	 * @return A primer containing the ingredients, recipe width and recipe height specified in the JSON object.
	 */
	public static ShapedPrimer parseShaped(final JsonObject json) {
		try {
			@SuppressWarnings("unchecked")
			final Map<String, Ingredient> key = (Map<String, Ingredient>) KEY_FROM_JSON.invoke(null, JSONUtils.getAsJsonObject(json, "key"));

			final String[] pattern = (String[]) SHRINK.invoke(null, PATTERN_FROM_JSON.invoke(null, JSONUtils.getAsJsonArray(json, "pattern")));

			final int recipeWidth = pattern[0].length();
			final int recipeHeight = pattern.length;

			@SuppressWarnings("unchecked")
			final NonNullList<Ingredient> ingredients = (NonNullList<Ingredient>) DISSOLVE_PATTERN.invoke(null, pattern, key, recipeWidth, recipeHeight);

			return new ShapedPrimer(ingredients, recipeWidth, recipeHeight);
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to parse shaped recipe", e);
		}
	}

	/**
	 * Parse the input of a shapeless recipe.
	 *
	 * @param json The recipe's JSON object
	 * @return A NonNullList containing the ingredients specified in the JSON object
	 */
	public static NonNullList<Ingredient> parseShapeless(final JsonObject json) {
		final NonNullList<Ingredient> ingredients = NonNullList.create();
		for (final JsonElement element : JSONUtils.getAsJsonArray(json, "ingredients"))
			ingredients.add(CraftingHelper.getIngredient(element));

		if (ingredients.isEmpty())
			throw new JsonParseException("No ingredients for shapeless recipe");

		return ingredients;
	}

	public static class ShapedPrimer {
		private final NonNullList<Ingredient> ingredients;
		private final int recipeWidth;
		private final int recipeHeight;

		public ShapedPrimer(final NonNullList<Ingredient> ingredients, final int recipeWidth, final int recipeHeight) {
			this.ingredients = ingredients;
			this.recipeWidth = recipeWidth;
			this.recipeHeight = recipeHeight;
		}

		public NonNullList<Ingredient> getIngredients() {
			return ingredients;
		}

		public int getRecipeWidth() {
			return recipeWidth;
		}

		public int getRecipeHeight() {
			return recipeHeight;
		}
	}
}
