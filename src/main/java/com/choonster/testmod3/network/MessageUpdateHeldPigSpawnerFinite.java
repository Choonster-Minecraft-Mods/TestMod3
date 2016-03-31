package com.choonster.testmod3.network;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import com.choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import com.choonster.testmod3.capability.pigspawner.CapabilityPigSpawner;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Sent from the server to update the values of the {@link IPigSpawnerFinite} for the player's held item.
 *
 * @author Choonster
 */
public class MessageUpdateHeldPigSpawnerFinite implements IMessage {
	/**
	 * The pig spawner's current number of pigs.
	 */
	private int numPigs;

	/**
	 * The hand holding the pig spawner.
	 */
	private EnumHand hand;

	public MessageUpdateHeldPigSpawnerFinite() {
	}

	public MessageUpdateHeldPigSpawnerFinite(IPigSpawnerFinite pigSpawner, EnumHand hand) {
		this.numPigs = pigSpawner.getNumPigs();
		this.hand = hand;
	}

	/**
	 * Convert from the supplied buffer into your specific message type
	 *
	 * @param buf The buffer
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		numPigs = buf.readInt();
		hand = EnumHand.values()[buf.readByte()];
	}

	/**
	 * Deconstruct your message into the supplied byte buffer
	 *
	 * @param buf The buffer
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(numPigs);
		buf.writeByte(hand.ordinal());
	}

	/**
	 * The message handler.
	 */
	public static class Handler implements IMessageHandler<MessageUpdateHeldPigSpawnerFinite, IMessage> {

		/**
		 * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
		 * is needed.
		 *
		 * @param message The message
		 * @param ctx     The message context
		 * @return an optional return message
		 */
		@Override
		public IMessage onMessage(final MessageUpdateHeldPigSpawnerFinite message, final MessageContext ctx) {
			// Using Minecraft or FMLClientHandler inside the lambda causes the server to attempt to load them, crashing it.
			// This can be worked around by using an anonymous class instead of a lambda or the mod's sided proxy instead of
			// Minecraft/FMLClientHandler.
			Minecraft.getMinecraft().addScheduledTask(() -> {
				final EntityPlayer player = TestMod3.proxy.getClientPlayer();
				final IPigSpawner pigSpawner = CapabilityPigSpawner.getPigSpawner(player.getHeldItem(message.hand));

				if (pigSpawner instanceof IPigSpawnerFinite) {
					final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) pigSpawner;

					Logger.info(CapabilityPigSpawner.LOG_MARKER, "Received finite pig spawner from server. %s - New: %d - Old: %d", pigSpawner, message.numPigs, pigSpawnerFinite.getNumPigs());

					pigSpawnerFinite.setNumPigs(message.numPigs);
				} else {
					Logger.info(CapabilityPigSpawner.LOG_MARKER, "Received non-finite pig spawner from server: %s", pigSpawner);
				}
			});

			return null;
		}
	}
}
