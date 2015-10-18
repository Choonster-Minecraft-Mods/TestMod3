package com.choonster.testmod3.util;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

/**
 * A class that holds a translation key and format arguments that can be translated for display purposes.
 */
public class TranslatableMessage {
	public final String translationKey;
	public final Object[] formatArgs;

	public TranslatableMessage(String translationKey, Object... formatArgs) {
		this.translationKey = translationKey;
		this.formatArgs = formatArgs;
	}

	/**
	 * Convert this message to a {@link ChatComponentTranslation}.
	 *
	 * @return The chat component
	 */
	public IChatComponent toChatComponent() {
		return new ChatComponentTranslation(translationKey, formatArgs);
	}

	/**
	 * Convert this message to a translated string.
	 *
	 * @return The translated string
	 */
	public String toTranslatedString() {
		return StatCollector.translateToLocalFormatted(translationKey, formatArgs);
	}
}
