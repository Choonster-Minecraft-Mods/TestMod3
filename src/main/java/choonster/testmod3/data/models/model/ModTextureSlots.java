package choonster.testmod3.data.models.model;

import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Creates this mod's {@link TextureSlot TextureSlots}.
 *
 * @author Choonster
 */
public class ModTextureSlots {
	private static final Method CREATE = ObfuscationReflectionHelper.findMethod(
			TextureSlot.class,
			"create",
			String.class
	);

	public static final TextureSlot BASE = create("base");
	public static final TextureSlot PLANE = create("plane");
	public static final TextureSlot CHEST = create("chest");

	private static TextureSlot create(final String id) {
		try {
			return (TextureSlot) CREATE.invoke(null, id);
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to create TextureSlot", e);
		}
	}
}
