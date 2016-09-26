package choonster.testmod3.network;

import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.CapabilityLock;
import choonster.testmod3.client.gui.GuiLock;
import choonster.testmod3.item.ItemKey;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

/**
 * Sent to the client by {@link ItemKey} to open {@link GuiLock}.
 *
 * @author Choonster
 */
public class MessageOpenLockGui implements IMessage {
	private BlockPos pos;
	private boolean hasFacing;
	private EnumFacing facing;

	@SuppressWarnings("unused")
	public MessageOpenLockGui() {
	}

	public MessageOpenLockGui(BlockPos pos, @Nullable EnumFacing facing) {
		this.pos = pos;
		this.facing = facing;
		this.hasFacing = facing != null;
	}

	/**
	 * Convert from the supplied buffer into your specific message type
	 *
	 * @param buf The buffer
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		hasFacing = buf.readBoolean();

		if (hasFacing) {
			facing = EnumFacing.getFront(buf.readUnsignedByte());
		}
	}

	/**
	 * Deconstruct your message into the supplied byte buffer
	 *
	 * @param buf The buffer
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		buf.writeBoolean(hasFacing);

		if (hasFacing) {
			buf.writeByte(facing.getIndex());
		}
	}

	public static class Handler implements IMessageHandler<MessageOpenLockGui, IMessage> {

		/**
		 * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
		 * is needed.
		 *
		 * @param message The message
		 * @param ctx     The message context
		 * @return an optional return message
		 */
		@Override
		public IMessage onMessage(MessageOpenLockGui message, MessageContext ctx) {
			final Minecraft minecraft = Minecraft.getMinecraft();

			minecraft.addScheduledTask(() -> {
				final ILock lock = CapabilityLock.getLock(minecraft.theWorld, message.pos, message.facing);
				if (lock != null) {
					minecraft.displayGuiScreen(new GuiLock(lock, message.pos, message.facing));
				}
			});

			return null;
		}
	}
}
