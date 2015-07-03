package com.choonster.testmod3.util;

import java.util.function.Function;

public class StringUtils {

	/**
	 * Builds a String by applying the specified Function to each digit of the number.
	 *
	 * @param function The function to call for each digit
	 * @param number   The number
	 * @return The built String
	 */
	private static String buildDigitString(Function<Integer, char[]> function, int number) {
		// 0 requires a special case
		if (number == 0) return new String(function.apply(0));

		boolean isNegative = number < 0;

		number = Math.abs(number);

		StringBuilder builder = new StringBuilder();

		// Get each digit of the number
		// Based on this StackOverflow answer from Martin B: http://stackoverflow.com/a/3118505

		while (number > 0) {
			builder.append(function.apply(number % 10));
			number /= 10;
		}

		if (isNegative) {
			builder.append("₋");
		}

		return builder.reverse().toString();
	}

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
		return buildDigitString(StringUtils::subscriptForDigit, number);
	}

	private static final char[] SUPERSCRIPT_1 = "¹".toCharArray(), SUPERSCRIPT_2 = "²".toCharArray(), SUPERSCRIPT_3 = "³".toCharArray();

	/**
	 * Gets the superscript character (U+00B9, U+00B2, U+00B3, U+2070, U+2074 - U+2079) for the specified digit.
	 *
	 * @param digit The digit (must be in range 0-9)
	 * @return A char array representing the superscript character
	 */
	private static char[] superscriptForDigit(int digit) {
		// Superscript 1, 2 and 3 require a special case since they're outside the U+207x range
		switch (digit) {
			case 1:
				return SUPERSCRIPT_1;
			case 2:
				return SUPERSCRIPT_2;
			case 3:
				return SUPERSCRIPT_3;
			default:
				return Character.toChars(0x2070 + digit);
		}
	}

	/**
	 * Gets a superscript String from the specified number using Unicode superscript characters (U+00B9, U+00B2, U+00B3, U+2070, U+2074 - U+2079, U+207B).
	 *
	 * @param number The number
	 * @return The subscript String
	 */
	public static String superscript(int number) {
		return buildDigitString(StringUtils::superscriptForDigit, number);
	}
}
