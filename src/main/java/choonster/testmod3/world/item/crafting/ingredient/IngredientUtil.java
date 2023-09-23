package choonster.testmod3.world.item.crafting.ingredient;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Utility methods for {@link Ingredient Ingredients}
 *
 * @author Choonster
 */
public class IngredientUtil {
	/**
	 * Serialises an object to JSON using a {@link Codec}.
	 *
	 * @param codec The codec
	 * @param input The input
	 * @param <T>   The ingredient type
	 * @return The serialised JSON
	 */
	public static <T> JsonElement toJson(final Codec<T> codec, final T input) {
		return Util.getOrThrow(codec.encodeStart(JsonOps.INSTANCE, input), IllegalStateException::new);
	}
}
