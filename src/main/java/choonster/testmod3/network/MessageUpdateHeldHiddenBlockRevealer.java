package choonster.testmod3.network;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.hiddenblockrevealer.CapabilityHiddenBlockRevealer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Sent from the server to update the value of the {@link IHiddenBlockRevealer} for the player's held item.
 *
 * @author Choonster
 */
public class MessageUpdateHeldHiddenBlockRevealer implements IMessage {

	/**
	 * Whether to reveal hidden blocks.
	 */
	private boolean revealHiddenBlocks;

	/**
	 * The hand holding the {@link IHiddenBlockRevealer}
	 */
	private EnumHand hand;

	@SuppressWarnings("unused")
	public MessageUpdateHeldHiddenBlockRevealer() {
	}

	public MessageUpdateHeldHiddenBlockRevealer(IHiddenBlockRevealer hiddenBlockRevealer, EnumHand hand) {
		this.revealHiddenBlocks = hiddenBlockRevealer.revealHiddenBlocks();
		this.hand = hand;
	}

	/**
	 * Convert from the supplied buffer into your specific message type
	 *
	 * @param buf The buffer
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		revealHiddenBlocks = buf.readBoolean();
		hand = EnumHand.values()[buf.readByte()];
	}

	/**
	 * Deconstruct your message into the supplied byte buffer
	 *
	 * @param buf The buffer
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(revealHiddenBlocks);
		buf.writeByte(hand.ordinal());
	}

	/**
	 * The message handler.
	 */
	public static class Handler implements IMessageHandler<MessageUpdateHeldHiddenBlockRevealer, IMessage> {

		/**
		 * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
		 * is needed.
		 *
		 * @param message The message
		 * @param ctx     The message context
		 * @return an optional return message
		 */
		@Override
		public IMessage onMessage(MessageUpdateHeldHiddenBlockRevealer message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				final EntityPlayer player = TestMod3.proxy.getClientPlayer();
				if (player != null) {
					final IHiddenBlockRevealer hiddenBlockRevealer = CapabilityHiddenBlockRevealer.getHiddenBlockRevealer(player.getHeldItem(message.hand));
					if (hiddenBlockRevealer != null) {
						hiddenBlockRevealer.setRevealHiddenBlocks(message.revealHiddenBlocks);
					}
				}
			});

			return null;
		}
	}
}
