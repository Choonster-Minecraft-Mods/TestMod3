package choonster.testmod3.network.capability.lastusetime;

import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Functions used by the {@link ILastUseTime} capability update message.
 *
 * @author Choonster
 */
class LastUseTimeFunctions {
	static long convertLastUseTimeToLastUseTimeValue(final ILastUseTime lastUseTime) {
		return lastUseTime.get();
	}

	static long decodeLastUseTimeValue(final FriendlyByteBuf buffer) {
		return buffer.readLong();
	}

	static void encodeLastUseTimeValue(final long lastUseTimeValue, final FriendlyByteBuf buffer) {
		buffer.writeLong(lastUseTimeValue);
	}

	static void applyLastUseTimeValueToLastUseTime(final ILastUseTime lastUseTime, final long lastUseTimeValue) {
		lastUseTime.set(lastUseTimeValue);
	}
}
