package choonster.testmod3.util;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

/**
 * Utility methods for reflection.
 *
 * @author Choonster
 */
public class ReflectionUtil {
	/**
	 * Finds the field with the specified name in the given class and makes it accessible.
	 * Note: For performance, store the returned value and avoid calling this repeatedly.
	 * <p>
	 * Throws an exception if the field is not found.
	 * <p>
	 * This is a copy of Forge's ObfuscationReflectionHelper.findField.
	 *
	 * @param clazz   The class to find the field on.
	 * @param srgName The SRG (obfuscated) name of the field to find(e.g. "field_146485_f").
	 * @return The field
	 */
	public static Field findField(final Class<?> clazz, final String srgName) {
		try {
			final Field field = clazz.getDeclaredField(ObfuscationReflectionHelper.remapName(srgName));
			field.setAccessible(true);
			return field;
		} catch (final Exception e1) {
			throw new UnableToFindFieldException(e1);
		}
	}

	public static class UnableToFindFieldException extends RuntimeException {
		private UnableToFindFieldException(final Exception e) {
			super(e);
		}
	}
}
