package choonster.testmod3.network.capability.hiddenblock;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.hiddenblockrevealer.CapabilityHiddenBlockRevealer;
import choonster.testmod3.network.capability.MessageBulkUpdateContainerCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Updates the {@link IHiddenBlockRevealer} for each slot of a {@link Container}.
 *
 * @author Choonster
 */
public class MessageBulkUpdateContainerHiddenBlockRevealers extends MessageBulkUpdateContainerCapability<IHiddenBlockRevealer, Boolean> {

	@SuppressWarnings("unused")
	public MessageBulkUpdateContainerHiddenBlockRevealers() {
		super(CapabilityHiddenBlockRevealer.HIDDEN_BLOCK_REVEALER_CAPABILITY);
	}

	public MessageBulkUpdateContainerHiddenBlockRevealers(final int windowID, final NonNullList<ItemStack> items) {
		super(CapabilityHiddenBlockRevealer.HIDDEN_BLOCK_REVEALER_CAPABILITY, CapabilityHiddenBlockRevealer.DEFAULT_FACING, windowID, items);
	}

	/**
	 * Convert a capability handler instance to a data instance.
	 *
	 * @param hiddenBlockRevealer The handler
	 * @return The data instance
	 */
	@Override
	protected Boolean convertCapabilityToData(final IHiddenBlockRevealer hiddenBlockRevealer) {
		return hiddenBlockRevealer.revealHiddenBlocks();
	}

	/**
	 * Read a data instance from the buffer.
	 *
	 * @param buf The buffer
	 */
	@Override
	protected Boolean readCapabilityData(final ByteBuf buf) {
		return buf.readBoolean();
	}

	/**
	 * Write a data instance to the buffer.
	 *
	 * @param buf  The buffer
	 * @param data The data instance
	 */
	@Override
	protected void writeCapabilityData(final ByteBuf buf, final Boolean data) {
		buf.writeBoolean(data);
	}

	public static class Handler extends MessageBulkUpdateContainerCapability.Handler<IHiddenBlockRevealer, Boolean, MessageBulkUpdateContainerHiddenBlockRevealers> {

		/**
		 * Apply the capability data from the data instance to the capability handler instance.
		 *
		 * @param hiddenBlockRevealer The capability handler instance
		 * @param data                The data instance
		 */
		@Override
		protected void applyCapabilityData(final IHiddenBlockRevealer hiddenBlockRevealer, final Boolean data) {
			hiddenBlockRevealer.setRevealHiddenBlocks(data);
		}
	}
}
