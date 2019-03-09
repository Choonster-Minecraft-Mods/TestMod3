package choonster.testmod3.network;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.chunkenergy.CapabilityChunkEnergy;
import choonster.testmod3.capability.chunkenergy.ChunkEnergy;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Sent from the server to update the energy value of an {@link IChunkEnergy}.
 *
 * @author Choonster
 */
public class MessageUpdateChunkEnergyValue {
	/**
	 * The position of the {@link IChunkEnergy}'s chunk.
	 */
	private final ChunkPos chunkPos;

	/**
	 * The new energy value.
	 */
	private final int energy;


	public MessageUpdateChunkEnergyValue(final IChunkEnergy chunkEnergy) {
		chunkPos = chunkEnergy.getChunkPos();
		energy = chunkEnergy.getEnergyStored();
	}

	private MessageUpdateChunkEnergyValue(final ChunkPos chunkPos, final int energy) {
		this.chunkPos = chunkPos;
		this.energy = energy;
	}

	public static MessageUpdateChunkEnergyValue decode(final PacketBuffer buffer) {
		return new MessageUpdateChunkEnergyValue(
				new ChunkPos(buffer.readInt(), buffer.readInt()),
				buffer.readInt()
		);
	}

	public static void encode(final MessageUpdateChunkEnergyValue message, final PacketBuffer buffer) {
		buffer.writeInt(message.chunkPos.x);
		buffer.writeInt(message.chunkPos.z);
		buffer.writeInt(message.energy);
	}

	public static void handle(final MessageUpdateChunkEnergyValue message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final Optional<World> optionalWorld = LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());

			optionalWorld.ifPresent(world ->
					CapabilityChunkEnergy.getChunkEnergy(world, message.chunkPos).ifPresent(chunkEnergy -> {
						if (!(chunkEnergy instanceof ChunkEnergy)) return;

						((ChunkEnergy) chunkEnergy).setEnergy(message.energy);
					})
			);
		});

		ctx.get().setPacketHandled(true);
	}
}
