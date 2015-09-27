package com.choonster.testmod3.tests;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class Test {
	protected Logger logger = LogManager.getLogger();

	private boolean allAssertionsPassed = true;

	protected void assertTrue(boolean value, String message) {
		if (!value) {
			allAssertionsPassed = false;
			logger.warn("Assertion failed: %s", message);
		}
	}

	protected void assertFalse(boolean value, String message) {
		if (value) {
			allAssertionsPassed = false;
			logger.warn("Assertion failed: %s");
		}
	}

	public final void test() {
		runTest();

		if (allAssertionsPassed) {
			logger.info("All assertions passed");
		}
	}

	protected abstract void runTest();
}
