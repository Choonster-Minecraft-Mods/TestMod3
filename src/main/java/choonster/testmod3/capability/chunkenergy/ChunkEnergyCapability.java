package choonster.testmod3.capability.chunkenergy;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.SerializableCapabilityProvider;
import choonster.testmod3.network.UpdateChunkEnergyValueMessage;
import choonster.testmod3.util.CapabilityNotPresentException;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

/**
 * Capability to store per-chunk energy values.
 *
 * @author Choonster
 */
public class ChunkEnergyCapability {
	/**
	 * The {@link IChunkEnergy} {@link Capability} instance.
	 */
	public static final Capability<IChunkEnergy> CHUNK_ENERGY_CHUNK_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	/**
	 * The default {@link Direction} to use for this capability.
	 */
	public static final Direction DEFAULT_FACING = null;

	/**
	 * The default capacity of an {@link IChunkEnergy}.
	 */
	public static final int DEFAULT_CAPACITY = 1000;

	/**
	 * The ID of this capability.
	 */
	public static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "chunk_energy");

	public static void register(final RegisterCapabilitiesEvent event) {
		event.register(IChunkEnergy.class);
	}

	/**
	 * Get the {@link IChunkEnergy} for the {@link Level} and chunk position.
	 *
	 * @param level    The level
	 * @param chunkPos The chunk position
	 * @return A lazy optional containing the IChunkEnergy, if any
	 */
	public static LazyOptional<IChunkEnergy> getChunkEnergy(final Level level, final ChunkPos chunkPos) {
		return getChunkEnergy(level.getChunk(chunkPos.x, chunkPos.z));
	}

	/**
	 * Get the {@link IChunkEnergy} for the chunk.
	 *
	 * @param chunk The chunk
	 * @return A lazy optional containing the IChunkEnergy, if any
	 */
	public static LazyOptional<IChunkEnergy> getChunkEnergy(final LevelChunk chunk) {
		return chunk.getCapability(CHUNK_ENERGY_CHUNK_CAPABILITY, DEFAULT_FACING);
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	@SuppressWarnings("unused")
	private static class EventHandler {
		@SubscribeEvent
		public static void attachChunkCapabilities(final AttachCapabilitiesEvent<LevelChunk> event) {
			final var chunk = event.getObject();
			final var chunkEnergy = new ChunkEnergy(DEFAULT_CAPACITY, chunk.getLevel(), chunk.getPos());
			event.addCapability(ID, new SerializableCapabilityProvider<>(CHUNK_ENERGY_CHUNK_CAPABILITY, DEFAULT_FACING, chunkEnergy));
		}

		/**
		 * Send the {@link IChunkEnergy} to the client when a player starts watching the chunk.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void chunkWatch(final ChunkWatchEvent.Watch event) {
			final var player = event.getPlayer();
			final var chunkEnergy = getChunkEnergy(event.getLevel(), event.getPos()).orElseThrow(CapabilityNotPresentException::new);

			TestMod3.network.send(PacketDistributor.PLAYER.with(() -> player), new UpdateChunkEnergyValueMessage(chunkEnergy));
		}
	}
}
