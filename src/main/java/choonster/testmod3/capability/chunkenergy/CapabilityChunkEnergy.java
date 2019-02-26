package choonster.testmod3.capability.chunkenergy;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.CapabilityProviderSerializable;
import choonster.testmod3.network.MessageUpdateChunkEnergyValue;
import choonster.testmod3.util.CapabilityUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * Capability to store per-chunk energy values.
 *
 * @author Choonster
 */
public class CapabilityChunkEnergy {
	/**
	 * The {@link IChunkEnergy} {@link Capability} instance.
	 */
	@CapabilityInject(IChunkEnergy.class)
	public static final Capability<IChunkEnergy> CHUNK_ENERGY_CHUNK_CAPABILITY = Null();

	/**
	 * The default {@link EnumFacing} to use for this capability.
	 */
	public static final EnumFacing DEFAULT_FACING = null;

	/**
	 * The default capacity of an {@link IChunkEnergy}.
	 */
	public static final int DEFAULT_CAPACITY = 1000;

	/**
	 * The ID of this capability.
	 */
	private static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "chunk_energy");

	/**
	 * The ID of this capability.
	 */
	private static final String ID_STRING = ID.toString();

	public static void register() {
		CapabilityManager.INSTANCE.register(IChunkEnergy.class, new Capability.IStorage<IChunkEnergy>() {
			@Override
			public NBTBase writeNBT(final Capability<IChunkEnergy> capability, final IChunkEnergy instance, final EnumFacing side) {
				return new NBTTagInt(instance.getEnergyStored());
			}

			@Override
			public void readNBT(final Capability<IChunkEnergy> capability, final IChunkEnergy instance, final EnumFacing side, final NBTBase nbt) {
				if (!(instance instanceof ChunkEnergy))
					throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");

				((ChunkEnergy) instance).setEnergy(((NBTTagInt) nbt).getInt());
			}
		}, () -> null);
	}

	/**
	 * Get the {@link IChunkEnergy} for the {@link World} and chunk position.
	 *
	 * @param world    The World
	 * @param chunkPos The chunk position
	 * @return The IChunkEnergy, if any
	 */
	@Nullable
	public static IChunkEnergy getChunkEnergy(final World world, final ChunkPos chunkPos) {
		return getChunkEnergy(world.getChunk(chunkPos.x, chunkPos.z));
	}

	/**
	 * Get the {@link IChunkEnergy} for the chunk.
	 *
	 * @param chunk The chunk
	 * @return The IChunkEnergy, if any
	 */
	@Nullable
	public static IChunkEnergy getChunkEnergy(final Chunk chunk) {
		return CapabilityUtils.getCapability(chunk, CHUNK_ENERGY_CHUNK_CAPABILITY, DEFAULT_FACING);
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	@SuppressWarnings("unused")
	private static class EventHandler {
		@SubscribeEvent
		public static void attachChunkCapabilities(final AttachCapabilitiesEvent<Chunk> event) {
			final Chunk chunk = event.getObject();
			final IChunkEnergy chunkEnergy = new ChunkEnergy(DEFAULT_CAPACITY, chunk.getWorld(), chunk.getPos());
			event.addCapability(ID, new CapabilityProviderSerializable<>(CHUNK_ENERGY_CHUNK_CAPABILITY, DEFAULT_FACING, chunkEnergy));
		}

		/**
		 * Send the {@link IChunkEnergy} to the client when a player starts watching the chunk.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void chunkWatch(final ChunkWatchEvent.Watch event) {
			final EntityPlayerMP player = event.getPlayer();
			final Chunk chunk = event.getChunkInstance();
			if (chunk == null) return;

			final IChunkEnergy chunkEnergy = getChunkEnergy(chunk);
			if (chunkEnergy == null) return;

			TestMod3.network.sendTo(new MessageUpdateChunkEnergyValue(chunkEnergy), player);
		}
	}
}
