package choonster.testmod3.capability.chunkenergy;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.SerializableCapabilityProvider;
import choonster.testmod3.network.UpdateChunkEnergyValueMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * Capability to store per-chunk energy values.
 *
 * @author Choonster
 */
public class ChunkEnergyCapability {
	/**
	 * The {@link IChunkEnergy} {@link Capability} instance.
	 */
	@CapabilityInject(IChunkEnergy.class)
	public static final Capability<IChunkEnergy> CHUNK_ENERGY_CHUNK_CAPABILITY = Null();

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
	private static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "chunk_energy");

	public static void register() {
		CapabilityManager.INSTANCE.register(IChunkEnergy.class, new Capability.IStorage<IChunkEnergy>() {
			@Override
			public INBT writeNBT(final Capability<IChunkEnergy> capability, final IChunkEnergy instance, final Direction side) {
				return IntNBT.valueOf(instance.getEnergyStored());
			}

			@Override
			public void readNBT(final Capability<IChunkEnergy> capability, final IChunkEnergy instance, final Direction side, final INBT nbt) {
				if (!(instance instanceof ChunkEnergy))
					throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");

				((ChunkEnergy) instance).setEnergy(((IntNBT) nbt).getInt());
			}
		}, () -> null);
	}

	/**
	 * Get the {@link IChunkEnergy} for the {@link World} and chunk position.
	 *
	 * @param world    The World
	 * @param chunkPos The chunk position
	 * @return A lazy optional containing the IChunkEnergy, if any
	 */
	public static LazyOptional<IChunkEnergy> getChunkEnergy(final World world, final ChunkPos chunkPos) {
		return getChunkEnergy(world.getChunk(chunkPos.x, chunkPos.z));
	}

	/**
	 * Get the {@link IChunkEnergy} for the chunk.
	 *
	 * @param chunk The chunk
	 * @return A lazy optional containing the IChunkEnergy, if any
	 */
	public static LazyOptional<IChunkEnergy> getChunkEnergy(final Chunk chunk) {
		return chunk.getCapability(CHUNK_ENERGY_CHUNK_CAPABILITY, DEFAULT_FACING);
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	@SuppressWarnings("unused")
	private static class EventHandler {
		@SubscribeEvent
		public static void attachChunkCapabilities(final AttachCapabilitiesEvent<Chunk> event) {
			final Chunk chunk = event.getObject();
			final IChunkEnergy chunkEnergy = new ChunkEnergy(DEFAULT_CAPACITY, chunk.getWorld(), chunk.getPos());
			event.addCapability(ID, new SerializableCapabilityProvider<>(CHUNK_ENERGY_CHUNK_CAPABILITY, DEFAULT_FACING, chunkEnergy));
		}

		/**
		 * Send the {@link IChunkEnergy} to the client when a player starts watching the chunk.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void chunkWatch(final ChunkWatchEvent.Watch event) {
			final ServerPlayerEntity player = event.getPlayer();

			getChunkEnergy(event.getWorld(), event.getPos())
					.ifPresent((chunkEnergy) -> TestMod3.network.send(PacketDistributor.PLAYER.with(() -> player), new UpdateChunkEnergyValueMessage(chunkEnergy)));
		}
	}
}
