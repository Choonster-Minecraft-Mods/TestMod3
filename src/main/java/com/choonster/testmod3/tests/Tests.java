package com.choonster.testmod3.tests;

/**
 * Runs this mod's tests.
 *
 * @author Choonster
 */
public class Tests {
	public static boolean runTests() {
		boolean allTestsPassed;

		allTestsPassed = OreDictUtilTests.INSTANCE.test();
		//		NBTTagEndTests.INSTANCE.test();

		return allTestsPassed;
	}
}
