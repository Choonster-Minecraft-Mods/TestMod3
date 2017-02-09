package choonster.testmod3.network.capability.lastusetime;

import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.lastusetime.CapabilityLastUseTime;
import choonster.testmod3.network.capability.MessageUpdateContainerCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

import javax.annotation.Nullable;

/**
 * Updates the {@link ILastUseTime} for a single slot of a {@link Container}.
 *
 * @author Choonster
 */
public class MessageUpdateContainerLastUseTime extends MessageUpdateContainerCapability<ILastUseTime, Long> {
	@SuppressWarnings("unused")
	public MessageUpdateContainerLastUseTime() {
		super(CapabilityLastUseTime.LAST_USE_TIME_CAPABILITY);
	}

	public MessageUpdateContainerLastUseTime(final int windowID, final int slotNumber, final ILastUseTime lastUseTime) {
		super(CapabilityLastUseTime.LAST_USE_TIME_CAPABILITY, CapabilityLastUseTime.DEFAULT_FACING, windowID, slotNumber, lastUseTime);
	}

	/**
	 * Convert a capability handler instance to a data instance.
	 *
	 * @param lastUseTime The handler
	 * @return The data instance
	 */
	@Nullable
	@Override
	protected Long convertCapabilityToData(final ILastUseTime lastUseTime) {
		return lastUseTime.get();
	}

	/**
	 * Read a data instance from the buffer.
	 *
	 * @param buf The buffer
	 */
	@Override
	protected Long readCapabilityData(final ByteBuf buf) {
		return buf.readLong();
	}

	/**
	 * Write a data instance to the buffer.
	 *
	 * @param buf  The buffer
	 * @param data The data instance
	 */
	@Override
	protected void writeCapabilityData(final ByteBuf buf, final Long data) {
		buf.writeLong(data);
	}

	public static class Handler extends MessageUpdateContainerCapability.Handler<ILastUseTime, Long, MessageUpdateContainerLastUseTime> {

		/**
		 * Apply the capability data from the data instance to the capability handler instance.
		 *
		 * @param lastUseTime The capability handler instance
		 * @param data        The data
		 */
		@Override
		protected void applyCapabilityData(final ILastUseTime lastUseTime, final Long data) {
			lastUseTime.set(data);
		}
	}
}
