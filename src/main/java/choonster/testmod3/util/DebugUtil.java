package choonster.testmod3.util;

import java.util.stream.Stream;

/**
 * Miscellaneous debugging methods.
 *
 * @author Choonster
 */
public class DebugUtil {

	/**
	 * Get a {@link Throwable} with its stacktrace limited to the first {@code depth} elements of the current call stack.
	 *
	 * @param depth The number of elements
	 * @return The Throwable
	 */
	public static Throwable getStackTrace(final int depth) {
		final Throwable throwable = new Throwable("StackTrace Helper - NOT AN ERROR");

		final StackTraceElement[] stackTraceElements = Stream.of(throwable.getStackTrace())
				.skip(1) // Skip this method
				.limit(depth) // Take the specified number of elements
				.toArray(StackTraceElement[]::new);

		throwable.setStackTrace(stackTraceElements);

		return throwable;
	}
}
