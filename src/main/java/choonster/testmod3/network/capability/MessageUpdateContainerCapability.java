package choonster.testmod3.network.capability;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.CapabilityUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Base class for messages that update capability data for a single slot of a {@link Container}.
 *
 * @param <HANDLER> The capability handler type
 * @param <DATA>    The data type written to and read from the buffer
 * @author Choonster
 */
public abstract class MessageUpdateContainerCapability<HANDLER, DATA> {
	/**
	 * The {@link Capability} instance to update.
	 */
	protected final Capability<HANDLER> capability;

	/**
	 * The {@link EnumFacing} to get the capability handler from.
	 */
	@Nullable
	protected EnumFacing facing;

	/**
	 * The window ID of the {@link Container}.
	 */
	protected int windowID;

	/**
	 * The slot's index in the {@link Container}.
	 */
	protected int slotNumber;

	/**
	 * The capability data instance.
	 */
	protected DATA capabilityData;

	protected MessageUpdateContainerCapability(final Capability<HANDLER> capability) {
		this.capability = capability;
	}

	protected MessageUpdateContainerCapability(final Capability<HANDLER> capability, @Nullable final EnumFacing facing, final int windowID, final int slotNumber, final HANDLER handler) {
		this.capability = capability;
		this.facing = facing;
		this.windowID = windowID;
		this.slotNumber = slotNumber;
		capabilityData = convertCapabilityToData(handler);
	}

	public final void fromBytes(final PacketBuffer buffer) {
		windowID = buffer.readInt();
		slotNumber = buffer.readInt();

		final int facingIndex = buffer.readByte();
		if (facingIndex >= 0) {
			facing = EnumFacing.byIndex(facingIndex);
		} else {
			facing = null;
		}

		final boolean hasData = buffer.readBoolean();
		if (hasData) {
			capabilityData = readCapabilityData(buffer);
		}
	}

	public final void toBytes(final PacketBuffer buffer) {
		buffer.writeInt(windowID);
		buffer.writeInt(slotNumber);

		if (facing != null) {
			buffer.writeByte(facing.getIndex());
		} else {
			buffer.writeByte(-1);
		}

		buffer.writeBoolean(hasData());
		writeCapabilityData(buffer, capabilityData);
	}

	/**
	 * Is there any capability data to sync?
	 *
	 * @return Is there any capability data to sync?
	 */
	public final boolean hasData() {
		return capabilityData != null;
	}

	/**
	 * Convert the capability handler instance to a data instance.
	 *
	 * @param handler The handler
	 * @return The data instance
	 */
	@Nullable
	protected abstract DATA convertCapabilityToData(HANDLER handler);

	/**
	 * Read the capability data from the buffer.
	 *
	 * @param buf The buffer
	 * @return The data instance
	 */
	protected abstract DATA readCapabilityData(final ByteBuf buf);

	/**
	 * Write the capability data to the buffer.
	 *
	 * @param buf  The buffer
	 * @param data The data instance
	 */
	protected abstract void writeCapabilityData(final ByteBuf buf, DATA data);

	public abstract static class Handler<HANDLER, DATA, MESSAGE extends MessageUpdateContainerCapability<HANDLER, DATA>> {

		/**
		 * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
		 * is needed.
		 *
		 * @param message The message
		 * @param ctx     The message context
		 * @return an optional return message
		 */
		@Nullable
		public final void onMessage(final MESSAGE message, final ChanCon ctx) {
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

				final ItemStack originalStack = container.getSlot(message.slotNumber).getStack();
				final HANDLER originalHandler = CapabilityUtils.getCapability(originalStack, message.capability, message.facing);
				if (originalHandler != null) {
					final ItemStack newStack = originalStack.copy();

					final HANDLER newHandler = CapabilityUtils.getCapability(newStack, message.capability, message.facing);
					assert newHandler != null;

					applyCapabilityData(newHandler, message.capabilityData);

					if (!originalHandler.equals(newHandler)) {
						container.putStackInSlot(message.slotNumber, newStack);
					}
				}
			});

			return null;
		}

		/**
		 * Apply the capability data from the data instance to the capability handler instance.
		 *
		 * @param handler The capability handler instance
		 * @param data    The data
		 */
		protected abstract void applyCapabilityData(final HANDLER handler, final DATA data);
	}
}
