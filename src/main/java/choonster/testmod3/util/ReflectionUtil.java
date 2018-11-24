package choonster.testmod3.util;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Utility methods for reflection.
 *
 * @author Choonster
 */
public class ReflectionUtil {

	/**
	 * Get a {@link MethodHandle} for a method.
	 *
	 * @param clazz          The class to find the method on.
	 * @param srgName        The obfuscated name of the method to find.
	 * @param returnType     The return type of the method to find.
	 * @param parameterTypes The parameter types of the method to find.
	 * @return The MethodHandle
	 */
	public static MethodHandle findMethod(final Class<?> clazz, final String srgName, final Class<?> returnType, final Class<?>... parameterTypes) {
		final Method method = ObfuscationReflectionHelper.findMethod(clazz, srgName, returnType, parameterTypes);
		try {
			return MethodHandles.lookup().unreflect(method);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to create MethodHandle for method", e);
		}
	}

	/**
	 * Get a {@link MethodHandle} for a field's getter.
	 *
	 * @param clazz   The class
	 * @param srgName The obfuscated name of the field to find.
	 * @return The MethodHandle
	 */
	public static MethodHandle findFieldGetter(final Class<?> clazz, final String srgName) {
		final Field field = ObfuscationReflectionHelper.findField(clazz, srgName);

		try {
			return MethodHandles.lookup().unreflectGetter(field);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Unable to create MethodHandle for field getter", e);
		}
	}

	/**
	 * Get a {@link MethodHandle} for a field's setter.
	 *
	 * @param clazz   The class
	 * @param srgName The obfuscated name of the field to find.
	 * @return The MethodHandle
	 */
	public static MethodHandle findFieldSetter(final Class<?> clazz, final String srgName) {
		final Field field = ObfuscationReflectionHelper.findField(clazz, srgName);

		try {
			return MethodHandles.lookup().unreflectSetter(field);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Unable to create MethodHandle for field setter", e);
		}
	}
}
