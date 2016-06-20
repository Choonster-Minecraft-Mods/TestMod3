package choonster.testmod3.tests;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class that runs various tests from {@link #runTest()}.
 * <p>
 * This exists because Minecraft/Forge don't support regular unit testing libraries.
 *
 * @author Choonster
 */
public abstract class Test {
	protected final Logger logger = LogManager.getLogger(this);

	private boolean allAssertionsPassed = true;

	protected void assertTrue(boolean value, String message) {
		if (!value) {
			allAssertionsPassed = false;
			logger.warn(String.format("Assertion failed: %s", message));
		}
	}

	protected void assertFalse(boolean value, String message) {
		if (value) {
			allAssertionsPassed = false;
			logger.warn(String.format("Assertion failed: %s", message));
		}
	}

	protected void exceptionThrown(Exception e, String message) {
		allAssertionsPassed = false;
		logger.warn(message, e);
	}

	protected void assertEqual(Object expected, Object actual) {
		if (!expected.equals(actual)) {
			allAssertionsPassed = false;
			logger.warn(String.format("Objects not equal. Expected: %s, Actual: %s", expected, actual));
		}
	}

	public final boolean test() {
		runTest();

		if (allAssertionsPassed) {
			logger.info("All assertions passed");
		}

		return allAssertionsPassed;
	}

	protected abstract void runTest();
}
