package com.choonster.testmod3.util;

public class StringUtils {

	/**
	 * Gets the Unicode subscript character (U+2080 - U+2089) for the specified digit.
	 *
	 * @param digit The digit (must be in range 0-9)
	 * @return A char array representing the subscript character
	 */
	private static char[] subscriptForDigit(int digit) {
		return Character.toChars(0x2080 + digit);
	}

	/**
	 * Gets a subscript String from the specified number using Unicode subscript characters (U+2080 - U+2089, U+208B).
	 *
	 * @param number The number
	 * @return The subscript String
	 */
	public static String subscript(int number) {
		// 0 requires a special case
		if (number == 0) return new String(subscriptForDigit(0));

		boolean isNegative = number < 0;

		number = Math.abs(number);

		StringBuilder builder = new StringBuilder();

		// Get each digit of the number
		// Based on this StackOverflow answer from Martin B: http://stackoverflow.com/a/3118505

		while (number > 0) {
			builder.append(subscriptForDigit(number % 10));
			number /= 10;
		}

		if (isNegative) {
			builder.append("₋");
		}

		return builder.reverse().toString();
	}
}
