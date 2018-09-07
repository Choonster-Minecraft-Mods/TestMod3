package choonster.testmod3.network.capability;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.CapabilityUtils;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Base class for messages that update the capability data for each slot of a {@link Container}.
 * <p>
 * The {@link HANDLER} type must override {@link Object#equals(Object)} to perform a value comparison and
 * {@link Object#hashCode()} to generate a hash code based on the values used in
 * {@link Object#equals(Object)}.
 *
 * @param <HANDLER> The capability handler type
 * @param <DATA>    The data type written to and read from the buffer
 * @author Choonster
 */
public abstract class MessageBulkUpdateContainerCapability<HANDLER, DATA> implements IMessage {
	/**
	 * The {@link Capability} instance to update.
	 */
	final Capability<HANDLER> capability;

	/**
	 * The {@link EnumFacing} to get the capability handler from.
	 */
	@Nullable
	EnumFacing facing;

	/**
	 * The window ID of the {@link Container}.
	 */
	int windowID;

	/**
	 * The capability data instances for each slot, indexed by their index in the original {@link List<ItemStack>}.
	 */
	final TIntObjectMap<DATA> capabilityData = new TIntObjectHashMap<>();

	protected MessageBulkUpdateContainerCapability(final Capability<HANDLER> capability) {
		this.capability = capability;
	}

	public MessageBulkUpdateContainerCapability(final Capability<HANDLER> capability, @Nullable final EnumFacing facing, final int windowID, final NonNullList<ItemStack> items) {
		this.capability = capability;
		this.facing = facing;
		this.windowID = windowID;

		for (int index = 0; index < items.size(); index++) {
			final ItemStack stack = items.get(index);

			final HANDLER handler = CapabilityUtils.getCapability(stack, capability, facing);

			if (handler != null) {
				final DATA data = convertCapabilityToData(handler);

				if (data != null) {
					capabilityData.put(index, data);
				}
			}
		}
	}

	/**
	 * Convert from the supplied buffer into your specific message type
	 *
	 * @param buf The buffer
	 */
	@Override
	public final void fromBytes(final ByteBuf buf) {
		windowID = buf.readInt();

		final int facingIndex = buf.readByte();
		if (facingIndex >= 0) {
			facing = EnumFacing.byIndex(facingIndex);
		} else {
			facing = null;
		}

		final int numEntries = buf.readInt();
		for (int i = 0; i < numEntries; i++) {
			final int index = buf.readInt();
			final DATA data = readCapabilityData(buf);
			capabilityData.put(index, data);
		}
	}

	/**
	 * Deconstruct your message into the supplied byte buffer
	 *
	 * @param buf The buffer
	 */
	@Override
	public final void toBytes(final ByteBuf buf) {
		buf.writeInt(windowID);

		if (facing != null) {
			buf.writeByte(facing.getIndex());
		} else {
			buf.writeByte(-1);
		}

		buf.writeInt(capabilityData.size());
		capabilityData.forEachEntry((index, data) -> {
			buf.writeInt(index);
			writeCapabilityData(buf, data);

			return true;
		});
	}

	/**
	 * Is there any capability data to sync?
	 *
	 * @return Is there any capability data to sync?
	 */
	public final boolean hasData() {
		return !capabilityData.isEmpty();
	}

	/**
	 * Convert a capability handler instance to a data instance.
	 *
	 * @param handler The handler
	 * @return The data instance
	 */
	@Nullable
	protected abstract DATA convertCapabilityToData(final HANDLER handler);

	/**
	 * Read a data instance from the buffer.
	 *
	 * @param buf The buffer
	 * @return The data instance
	 */
	protected abstract DATA readCapabilityData(final ByteBuf buf);

	/**
	 * Write a data instance to the buffer.
	 *
	 * @param buf  The buffer
	 * @param data The data instance
	 */
	protected abstract void writeCapabilityData(final ByteBuf buf, DATA data);

	public abstract static class Handler<HANDLER, DATA, MESSAGE extends MessageBulkUpdateContainerCapability<HANDLER, DATA>> implements IMessageHandler<MESSAGE, IMessage> {

		/**
		 * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
		 * is needed.
		 *
		 * @param message The message
		 * @param ctx     The message context
		 * @return an optional return message
		 */
		@Nullable
		@Override
		public final IMessage onMessage(final MESSAGE message, final MessageContext ctx) {
			if (!message.hasData()) return null; // Don't do anything if no data was sent

			TestMod3.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				final EntityPlayer player = TestMod3.proxy.getPlayer(ctx);

				final Container container;
				if (message.windowID == 0) {
					container = player.inventoryContainer;
				} else if (message.windowID == player.openContainer.windowId) {
					container = player.openContainer;
				} else {
					return;
				}

				message.capabilityData.forEachEntry((index, data) -> {
					final ItemStack originalStack = container.getSlot(index).getStack();
					final HANDLER originalHandler = CapabilityUtils.getCapability(originalStack, message.capability, message.facing);
					if (originalHandler != null) {
						final ItemStack newStack = originalStack.copy();

						final HANDLER newHandler = CapabilityUtils.getCapability(newStack, message.capability, message.facing);
						assert newHandler != null;

						applyCapabilityData(newHandler, data);

						if (!originalHandler.equals(newHandler)) {
							container.putStackInSlot(index, newStack);
						}
					}

					return true;
				});
			});

			return null;
		}

		/**
		 * Apply the capability data from the data instance to the capability handler instance.
		 *
		 * @param handler The capability handler instance
		 * @param data    The data instance
		 */
		protected abstract void applyCapabilityData(final HANDLER handler, final DATA data);
	}
}
