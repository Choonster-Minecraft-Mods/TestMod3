package choonster.testmod3.util;

import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindFieldException;

import javax.annotation.Nullable;
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
	 * @param methodName     The name of the method to find (used in developer environments, e.g. "getWorldTime").
	 * @param methodObfName  The obfuscated name of the method to find (used in obfuscated environments, e.g. "func_72820_D").
	 *                       If the name you are looking for is on a class that is never obfuscated, this should be null.
	 * @param parameterTypes The parameter types of the method to find.
	 * @return The MethodHandle
	 */
	public static MethodHandle findMethod(final Class<?> clazz, final String methodName, @Nullable final String methodObfName, final Class<?>... parameterTypes) {
		final Method method = ReflectionHelper.findMethod(clazz, methodName, methodObfName, parameterTypes);
		try {
			return MethodHandles.lookup().unreflect(method);
		} catch (IllegalAccessException e) {
			throw new ReflectionHelper.UnableToFindMethodException(e);
		}
	}

	/**
	 * Get an array of field names to pass to {@link ReflectionHelper#findField}.
	 * <p>
	 * Passing a {@code null} field name to the method would throw a {@link UnableToFindFieldException} with a
	 * {@link NullPointerException} as the cause instead of a {@link NoSuchFieldException}.
	 *
	 * @param fieldName    The name of the field
	 * @param fieldObfName The obfuscated name of the field
	 * @return An array of field names
	 */
	private static String[] getFieldNameArray(final String fieldName, @Nullable final String fieldObfName) {
		if (fieldObfName != null) {
			return new String[]{fieldName, fieldObfName};
		} else {
			return new String[]{fieldName};
		}
	}

	/**
	 * Get a {@link MethodHandle} for a field's getter.
	 *
	 * @param clazz        The class
	 * @param fieldName    The name of the field to find (used in developer environments, e.g. "worldInfo").
	 * @param fieldObfName The obfuscated name of the field to find (used in obfuscated environments, e.g. "field_72986_A").
	 *                     If the name you are looking for is on a class that is never obfuscated, this should be null.
	 * @return The MethodHandle
	 */
	public static MethodHandle findFieldGetter(final Class<?> clazz, final String fieldName, @Nullable final String fieldObfName) {
		final Field field = ReflectionHelper.findField(clazz, getFieldNameArray(fieldName, fieldObfName));

		try {
			return MethodHandles.lookup().unreflectGetter(field);
		} catch (IllegalAccessException e) {
			throw new ReflectionHelper.UnableToAccessFieldException(new String[0], e);
		}
	}

	/**
	 * Get a {@link MethodHandle} for a field's setter.
	 *
	 * @param clazz        The class
	 * @param fieldName    The name of the field to find (used in developer environments, e.g. "worldInfo").
	 * @param fieldObfName The obfuscated name of the field to find (used in obfuscated environments, e.g. "field_72986_A").
	 *                     If the name you are looking for is on a class that is never obfuscated, this should be null.
	 * @return The MethodHandle
	 */
	public static MethodHandle findFieldSetter(Class<?> clazz, final String fieldName, @Nullable final String fieldObfName) {
		final Field field = ReflectionHelper.findField(clazz, getFieldNameArray(fieldName, fieldObfName));

		try {
			return MethodHandles.lookup().unreflectSetter(field);
		} catch (IllegalAccessException e) {
			throw new ReflectionHelper.UnableToAccessFieldException(new String[0], e);
		}
	}
}
