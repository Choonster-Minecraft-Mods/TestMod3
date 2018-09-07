package choonster.testmod3.capability.chunkenergy;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergyHolder;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergyHolderModifiable;
import choonster.testmod3.capability.CapabilityProviderSerializable;
import choonster.testmod3.capability.CapabilityProviderSimple;
import choonster.testmod3.network.MessageUpdateChunkEnergyValue;
import choonster.testmod3.util.CapabilityUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
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
	 * The {@link IChunkEnergyHolder} {@link Capability} instance.
	 *
	 * @deprecated Use {@link IChunkEnergy} directly instead; TODO: Remove in 1.13
	 */
	@Deprecated
	@CapabilityInject(IChunkEnergyHolder.class)
	public static final Capability<IChunkEnergyHolder> CHUNK_ENERGY_CAPABILITY = Null();

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
		// TODO: Remove in 1.13
		//noinspection deprecation
		CapabilityManager.INSTANCE.register(IChunkEnergyHolder.class, new Capability.IStorage<IChunkEnergyHolder>() {
			@Override
			public NBTBase writeNBT(final Capability<IChunkEnergyHolder> capability, final IChunkEnergyHolder instance, final EnumFacing side) {
				return new NBTTagCompound();
			}

			@Override
			public void readNBT(final Capability<IChunkEnergyHolder> capability, final IChunkEnergyHolder instance, final EnumFacing side, final NBTBase nbt) {

			}
		}, ChunkEnergyHolder::new);

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
	 * Get the {@link IChunkEnergyHolder} for the {@link World}, if any.
	 *
	 * @param world The World
	 * @return The IChunkEnergyHolder, if any
	 * @deprecated Use {@link IChunkEnergy} directly instead; TODO: Remove in 1.13
	 */
	@Deprecated
	@Nullable
	public static IChunkEnergyHolder getChunkEnergyHolder(final World world) {
		return CapabilityUtils.getCapability(world, CHUNK_ENERGY_CAPABILITY, DEFAULT_FACING);
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

		/**
		 * Attach the {@link IChunkEnergyHolder} capability to all {@link World}s.
		 *
		 * @param event The event
		 */
		@Deprecated
		@SubscribeEvent
		public static void attachCapabilities(final AttachCapabilitiesEvent<World> event) {
			final IChunkEnergyHolder chunkEnergyHolder = new ChunkEnergyHolder();
			event.addCapability(ID, new CapabilityProviderSimple<>(CHUNK_ENERGY_CAPABILITY, DEFAULT_FACING, chunkEnergyHolder));
		}

		@SubscribeEvent
		public static void attachChunkCapabilities(final AttachCapabilitiesEvent<Chunk> event) {
			final Chunk chunk = event.getObject();
			final IChunkEnergy chunkEnergy = new ChunkEnergy(DEFAULT_CAPACITY, chunk.getWorld(), chunk.getPos());
			event.addCapability(ID, new CapabilityProviderSerializable<>(CHUNK_ENERGY_CHUNK_CAPABILITY, DEFAULT_FACING, chunkEnergy));
		}

		/**
		 * Load the legacy {@link IChunkEnergy} data for a chunk when the chunk is loaded.
		 *
		 * @param event The event
		 * @deprecated {@link IChunkEnergy} is now a {@link Chunk} capability; TODO: Remove in 1.13
		 */
		@Deprecated
		@SubscribeEvent
		public static void chunkDataLoad(final ChunkDataEvent.Load event) {
			final World world = event.getWorld();
			final Chunk chunk = event.getChunk();
			final ChunkPos chunkPos = chunk.getPos();

			final IChunkEnergyHolder chunkEnergyHolder = getChunkEnergyHolder(world);
			if (!(chunkEnergyHolder instanceof IChunkEnergyHolderModifiable)) return;

			final IChunkEnergy iChunkEnergy = getChunkEnergy(chunk);
			if (!(iChunkEnergy instanceof ChunkEnergy)) return;

			final ChunkEnergy chunkEnergy = (ChunkEnergy) iChunkEnergy;

			final NBTTagCompound chunkData = event.getData();
			if (chunkData.hasKey(ID_STRING, Constants.NBT.TAG_INT)) {
				final NBTTagInt energyTag = (NBTTagInt) chunkData.getTag(ID_STRING);
				chunkEnergy.deserializeNBT(energyTag);
			}

			((IChunkEnergyHolderModifiable) chunkEnergyHolder).setChunkEnergy(chunkPos, chunkEnergy);
		}

		/**
		 * Add the {@link IChunkEnergy} for a chunk to the {@link IChunkEnergyHolder} if it doesn't already have one.
		 * <p>
		 * {@link ChunkDataEvent.Load} is never fired for client-side chunks, so this allows them to have a default
		 * {@link IChunkEnergy} added to the {@link IChunkEnergyHolder} when they load.
		 *
		 * @param event The event
		 * @deprecated {@link IChunkEnergy} is now a {@link Chunk} capability; TODO: Remove in 1.13
		 */
		@Deprecated
		@SubscribeEvent
		public static void chunkLoad(final ChunkEvent.Load event) {
			final World world = event.getWorld();
			final Chunk chunk = event.getChunk();
			final ChunkPos chunkPos = chunk.getPos();

			final IChunkEnergyHolder chunkEnergyHolder = getChunkEnergyHolder(world);
			if (!(chunkEnergyHolder instanceof IChunkEnergyHolderModifiable)) return;

			if (chunkEnergyHolder.getChunkEnergy(chunkPos) != null) return;

			final IChunkEnergy chunkEnergy = getChunkEnergy(chunk);
			if (chunkEnergy != null) {
				((IChunkEnergyHolderModifiable) chunkEnergyHolder).setChunkEnergy(chunkPos, chunkEnergy);
			}
		}

		/**
		 * Remove the {@link IChunkEnergy} for a chunk from the {@link IChunkEnergyHolder} when a chunk is unloaded.
		 *
		 * @param event The event
		 * @deprecated {@link IChunkEnergy} is now a {@link Chunk} capability; TODO: Remove in 1.13
		 */
		@Deprecated
		@SubscribeEvent
		public static void chunkUnload(final ChunkEvent.Unload event) {
			final IChunkEnergyHolder chunkEnergyHolder = getChunkEnergyHolder(event.getWorld());
			if (!(chunkEnergyHolder instanceof IChunkEnergyHolderModifiable)) return;

			((IChunkEnergyHolderModifiable) chunkEnergyHolder).removeChunkEnergy(event.getChunk().getPos());
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
