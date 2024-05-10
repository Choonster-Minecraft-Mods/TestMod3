package choonster.testmod3.network;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.chunkenergy.ChunkEnergy;
import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import choonster.testmod3.serialization.VanillaCodecs;
import choonster.testmod3.util.CapabilityNotPresentException;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.LogicalSide;

/**
 * Sent from the server to update the energy value of an {@link IChunkEnergy}.
 *
 * @param chunkPos The position of the {@link IChunkEnergy}'s chunk.
 * @param energy   The new energy value.
 * @author Choonster
 */
public record UpdateChunkEnergyValueMessage(ChunkPos chunkPos, int energy) {
	public static StreamCodec<RegistryFriendlyByteBuf, UpdateChunkEnergyValueMessage> STREAM_CODEC = StreamCodec.composite(
			VanillaCodecs.CHUNK_POS_STREAM_CODEC,
			UpdateChunkEnergyValueMessage::chunkPos,
			ByteBufCodecs.VAR_INT,
			UpdateChunkEnergyValueMessage::energy,
			UpdateChunkEnergyValueMessage::new
	);

	public UpdateChunkEnergyValueMessage(final IChunkEnergy chunkEnergy) {
		this(chunkEnergy.getChunkPos(), chunkEnergy.getEnergyStored());
	}

	public static void handle(final UpdateChunkEnergyValueMessage message, final CustomPayloadEvent.Context ctx) {
		final var optionalLevel = LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT);

		optionalLevel.ifPresent(world -> {
			final var iChunkEnergy = ChunkEnergyCapability
					.getChunkEnergy(world, message.chunkPos)
					.orElseThrow(CapabilityNotPresentException::new);

			if (iChunkEnergy instanceof final ChunkEnergy chunkEnergy) {
				chunkEnergy.setEnergy(message.energy);
			}
		});
	}
}
