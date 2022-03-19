package choonster.testmod3.util;

import choonster.testmod3.TestMod3;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Utility methods for logging.
 *
 * @author Choonster
 */
public class ModLogUtils {
	/**
	 * A base marker for this mod's log markers.
	 */
	public static final Marker MOD_MARKER = MarkerFactory.getMarker(TestMod3.MODID);

	/**
	 * Gets a Marker instance with the specified name that references {@link #MOD_MARKER}.
	 *
	 * @param name The name of the {@link Marker} object to return.
	 * @return The Marker
	 */
	public static Marker getMarker(final String name) {
		final var marker = MarkerFactory.getMarker(name);
		marker.add(MOD_MARKER);
		return marker;
	}
}
