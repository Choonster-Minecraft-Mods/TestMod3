package choonster.testmod3.world.item.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Utility methods for {@link Recipe}s.
 *
 * @author Choonster
 */
public class RecipeUtil {
	private static final Method KEY_FROM_JSON = ObfuscationReflectionHelper.findMethod(ShapedRecipe.class, /* keyFromJson */ "m_44210_", JsonObject.class);
	private static final Method SHRINK = ObfuscationReflectionHelper.findMethod(ShapedRecipe.class, /* shrink */ "m_44186_", String[].class);
	private static final Method PATTERN_FROM_JSON = ObfuscationReflectionHelper.findMethod(ShapedRecipe.class, /* patternFromJson */ "m_44196_", JsonArray.class);
	private static final Method DISSOLVE_PATTERN = ObfuscationReflectionHelper.findMethod(ShapedRecipe.class, /* dissolvePattern */ "m_44202_", String[].class, Map.class, int.class, int.class);

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
			@SuppressWarnings("unchecked") final var key = (Map<String, Ingredient>) KEY_FROM_JSON.invoke(null, GsonHelper.getAsJsonObject(json, "key"));

			final var pattern = (String[]) SHRINK.invoke(null, PATTERN_FROM_JSON.invoke(null, GsonHelper.getAsJsonArray(json, "pattern")));

			final var recipeWidth = pattern[0].length();
			final var recipeHeight = pattern.length;

			@SuppressWarnings("unchecked") final var ingredients = (NonNullList<Ingredient>) DISSOLVE_PATTERN.invoke(null, pattern, key, recipeWidth, recipeHeight);

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
		final var ingredients = NonNullList.<Ingredient>create();
		for (final var element : GsonHelper.getAsJsonArray(json, "ingredients")) {
			ingredients.add(CraftingHelper.getIngredient(element, false));
		}

		if (ingredients.isEmpty()) {
			throw new JsonParseException("No ingredients for shapeless recipe");
		}

		return ingredients;
	}

	public record ShapedPrimer(
			NonNullList<Ingredient> ingredients,
			int recipeWidth,
			int recipeHeight
	) {
	}
}
