package choonster.testmod3.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;

/**
 * Utility methods for JSON serialisation/deserialisation.
 *
 * @author Choonster
 */
public class ModJsonUtil {
	/**
	 * Serialises an object to JSON using a {@link Codec}.
	 *
	 * @param codec The codec
	 * @param input The input
	 * @param <T>   The input type
	 * @return The serialised JSON
	 */
	public static <T> JsonElement toJson(final Codec<T> codec, final T input) {
		return Util.getOrThrow(codec.encodeStart(JsonOps.INSTANCE, input), IllegalStateException::new);
	}
}
