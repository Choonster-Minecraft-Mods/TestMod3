package choonster.testmod3.network.capability.hiddenblock;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.hiddenblockrevealer.CapabilityHiddenBlockRevealer;
import choonster.testmod3.network.capability.MessageUpdateContainerCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

import javax.annotation.Nullable;

/**
 * Updates the {@link IHiddenBlockRevealer} for a single slot of a {@link Container}.
 *
 * @author Choonster
 */
public class MessageUpdateContainerHiddenBlockRevealer extends MessageUpdateContainerCapability<IHiddenBlockRevealer, Boolean> {

	@SuppressWarnings("unused")
	public MessageUpdateContainerHiddenBlockRevealer() {
		super(CapabilityHiddenBlockRevealer.HIDDEN_BLOCK_REVEALER_CAPABILITY);
	}

	public MessageUpdateContainerHiddenBlockRevealer(final int windowID, final int slotNumber, final IHiddenBlockRevealer hiddenBlockRevealer) {
		super(CapabilityHiddenBlockRevealer.HIDDEN_BLOCK_REVEALER_CAPABILITY, CapabilityHiddenBlockRevealer.DEFAULT_FACING, windowID, slotNumber, hiddenBlockRevealer);
	}

	/**
	 * Convert the capability handler instance to a data instance.
	 *
	 * @param hiddenBlockRevealer The handler
	 * @return The data instance
	 */
	@Nullable
	@Override
	protected Boolean convertCapabilityToData(final IHiddenBlockRevealer hiddenBlockRevealer) {
		return hiddenBlockRevealer.revealHiddenBlocks();
	}

	/**
	 * Read the capability data from the buffer.
	 *
	 * @param buf The buffer
	 * @return The data instance
	 */
	@Override
	protected Boolean readCapabilityData(final ByteBuf buf) {
		return buf.readBoolean();
	}

	/**
	 * Write the capability data to the buffer.
	 *
	 * @param buf  The buffer
	 * @param data The data instance
	 */
	@Override
	protected void writeCapabilityData(final ByteBuf buf, final Boolean data) {
		buf.writeBoolean(data);
	}

	public static class Handler extends MessageUpdateContainerCapability.Handler<IHiddenBlockRevealer, Boolean, MessageUpdateContainerHiddenBlockRevealer> {

		/**
		 * Apply the capability data from the message to the capability handler instance.
		 *
		 * @param hiddenBlockRevealer The IHiddenBlockRevealer
		 * @param data                The message
		 */
		@Override
		protected void applyCapabilityData(final IHiddenBlockRevealer hiddenBlockRevealer, final Boolean data) {
			hiddenBlockRevealer.setRevealHiddenBlocks(data);
		}
	}
}
