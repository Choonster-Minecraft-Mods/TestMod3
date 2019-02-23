package choonster.testmod3.util;

import choonster.testmod3.TestMod3;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * Utility methods for logging.
 *
 * @author Choonster
 */
public class LogUtil {
	/**
	 * A base marker for this mod's log markers.
	 */
	public static final Marker MOD_MARKER = MarkerManager.getMarker(TestMod3.MODID);

	/**
	 * Logs a debug message with parameters and a the stack trace of a {@link Throwable}.
	 *
	 * @param logger    The logger
	 * @param marker    The log marker
	 * @param throwable The throwable to log the stack trace of
	 * @param message   The message to log
	 * @param params    The parameters to the message
	 */
	public static void debug(final Logger logger, final Marker marker, final Throwable throwable, final String message, final Object... params) {
		logger.log(Level.DEBUG, marker, logger.getMessageFactory().newMessage(message, params), throwable);
	}
}
