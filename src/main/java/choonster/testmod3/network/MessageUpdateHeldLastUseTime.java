package choonster.testmod3.network;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.lastusetime.CapabilityLastUseTime;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

/**
 * Sent from the server to update the value of the {@link ILastUseTime} for the player's held item.
 *
 * @author Choonster
 */
public class MessageUpdateHeldLastUseTime implements IMessage {

	/**
	 * The last use time.
	 */
	private long lastUseTime;

	/**
	 * The hand holding the {@link ILastUseTime}
	 */
	private EnumHand hand;

	@SuppressWarnings("unused")
	public MessageUpdateHeldLastUseTime() {
	}

	public MessageUpdateHeldLastUseTime(ILastUseTime lastUseTime, EnumHand hand) {
		this.lastUseTime = lastUseTime.get();
		this.hand = hand;
	}

	/**
	 * Convert from the supplied buffer into your specific message type
	 *
	 * @param buf The buffer
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		lastUseTime = buf.readLong();
		hand = EnumHand.values()[buf.readByte()];
	}

	/**
	 * Deconstruct your message into the supplied byte buffer
	 *
	 * @param buf The buffer
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(lastUseTime);
		buf.writeByte(hand.ordinal());
	}

	/**
	 * The message handler.
	 */
	public static class Handler implements IMessageHandler<MessageUpdateHeldLastUseTime, IMessage> {

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
		public IMessage onMessage(MessageUpdateHeldLastUseTime message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				final EntityPlayer player = TestMod3.proxy.getClientPlayer();
				assert player != null;

				final ILastUseTime lastUseTime = CapabilityLastUseTime.getLastUseTime(player.getHeldItem(message.hand));
				if (lastUseTime != null) {
					lastUseTime.set(message.lastUseTime);
				}
			});

			return null;
		}
	}
}
