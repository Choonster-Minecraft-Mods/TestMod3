package choonster.testmod3.network;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.chunkenergy.CapabilityChunkEnergy;
import choonster.testmod3.capability.chunkenergy.ChunkEnergy;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

/**
 * Sent from the server to update the energy value of an {@link IChunkEnergy}.
 *
 * @author Choonster
 */
public class MessageUpdateChunkEnergyValue implements IMessage {
	/**
	 * The position of the {@link IChunkEnergy}'s chunk.
	 */
	private ChunkPos chunkPos;

	/**
	 * The new energy value.
	 */
	private int energy;

	@SuppressWarnings("unused")
	public MessageUpdateChunkEnergyValue() {
	}

	public MessageUpdateChunkEnergyValue(IChunkEnergy chunkEnergy) {
		this.chunkPos = chunkEnergy.getChunkPos();
		this.energy = chunkEnergy.getEnergyStored();
	}

	/**
	 * Convert from the supplied buffer into your specific message type
	 *
	 * @param buf The buffer
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		final int chunkX = buf.readInt();
		final int chunkZ = buf.readInt();
		chunkPos = new ChunkPos(chunkX, chunkZ);
		energy = buf.readInt();
	}

	/**
	 * Deconstruct your message into the supplied byte buffer
	 *
	 * @param buf The buffer
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(chunkPos.x);
		buf.writeInt(chunkPos.z);
		buf.writeInt(energy);
	}

	public static class Handler implements IMessageHandler<MessageUpdateChunkEnergyValue, IMessage> {

		/**
		 * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
		 * is needed.
		 *
		 * @param message The message
		 * @param ctx     The message context
		 * @return An optional return message
		 */
		@Nullable
		@Override
		public IMessage onMessage(MessageUpdateChunkEnergyValue message, MessageContext ctx) {
			TestMod3.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				final World world = TestMod3.proxy.getClientWorld();
				assert world != null;

				final IChunkEnergy chunkEnergy = CapabilityChunkEnergy.getChunkEnergy(world, message.chunkPos);
				if (!(chunkEnergy instanceof ChunkEnergy)) return;

				((ChunkEnergy) chunkEnergy).setEnergy(message.energy);
			});

			return null;
		}
	}
}
