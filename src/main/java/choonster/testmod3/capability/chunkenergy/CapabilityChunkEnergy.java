package choonster.testmod3.capability.chunkenergy;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergyHolder;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergyHolderModifiable;
import choonster.testmod3.capability.CapabilityProviderSimple;
import choonster.testmod3.util.CapabilityUtils;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

/**
 * Capability to store per-chunk energy values.
 *
 * @author Choonster
 */
public class CapabilityChunkEnergy {

	/**
	 * The {@link Capability} instance
	 */
	@CapabilityInject(IChunkEnergyHolder.class)
	public static final Capability<IChunkEnergyHolder> CHUNK_ENERGY_CAPABILITY = null;

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
		CapabilityManager.INSTANCE.register(IChunkEnergyHolder.class, new Capability.IStorage<IChunkEnergyHolder>() {
			@Override
			public NBTBase writeNBT(Capability<IChunkEnergyHolder> capability, IChunkEnergyHolder instance, EnumFacing side) {
				return new NBTTagCompound();
			}

			@Override
			public void readNBT(Capability<IChunkEnergyHolder> capability, IChunkEnergyHolder instance, EnumFacing side, NBTBase nbt) {

			}
		}, ChunkEnergyHolder::new);
	}

	/**
	 * Get the {@link IChunkEnergyHolder} for the {@link World}, if any.
	 *
	 * @param world The World
	 * @return The IChunkEnergyHolder, if any
	 */
	@Nullable
	public static IChunkEnergyHolder getChunkEnergyHolder(World world) {
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
	public static IChunkEnergy getChunkEnergy(World world, ChunkPos chunkPos) {
		final IChunkEnergyHolder chunkEnergyHolder = getChunkEnergyHolder(world);
		if (chunkEnergyHolder == null) return null;

		return chunkEnergyHolder.getChunkEnergy(chunkPos);
	}

	/**
	 * Get the {@link IChunkEnergy} for the chunk.
	 *
	 * @param chunk The chunk
	 * @return The IChunkEnergy, if any
	 */
	@Nullable
	public static IChunkEnergy getChunkEnergy(Chunk chunk) {
		return getChunkEnergy(chunk.getWorld(), chunk.getPos());
	}

	@Mod.EventBusSubscriber
	public static class EventHandler {

		/**
		 * Attach the {@link IChunkEnergyHolder} capability to all {@link World}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void attachCapabilities(AttachCapabilitiesEvent<World> event) {
			final IChunkEnergyHolder chunkEnergyHolder = new ChunkEnergyHolder();
			event.addCapability(ID, new CapabilityProviderSimple<>(chunkEnergyHolder, CHUNK_ENERGY_CAPABILITY, DEFAULT_FACING));
		}

		/**
		 * Load the {@link ChunkEnergy} for a chunk when the chunk is loaded.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void chunkDataLoad(ChunkDataEvent.Load event) {
			final IChunkEnergyHolder chunkEnergyHolder = getChunkEnergyHolder(event.getWorld());
			if (!(chunkEnergyHolder instanceof IChunkEnergyHolderModifiable)) return;

			final IChunkEnergy chunkEnergy = new ChunkEnergy(DEFAULT_CAPACITY);

			final NBTTagCompound chunkData = event.getData();
			final NBTTagInt energyTag = chunkData.hasKey(ID_STRING, Constants.NBT.TAG_INT) ? (NBTTagInt) chunkData.getTag(ID_STRING) : new NBTTagInt(DEFAULT_CAPACITY);
			chunkEnergy.deserializeNBT(energyTag);

			((IChunkEnergyHolderModifiable) chunkEnergyHolder).setChunkEnergy(event.getChunk().getPos(), chunkEnergy);
		}

		/**
		 * Save the {@link IChunkEnergy} for a chunk when a chunk is saved.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void chunkDataSave(ChunkDataEvent.Save event) {
			final IChunkEnergy chunkEnergy = getChunkEnergy(event.getChunk());
			if (chunkEnergy == null) return;

			event.getData().setTag(ID_STRING, chunkEnergy.serializeNBT());
		}

		/**
		 * Remove the {@link IChunkEnergy} for a chunk when a chunk is unloaded.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void chunkUnload(ChunkEvent.Unload event) {
			final IChunkEnergyHolder chunkEnergyHolder = getChunkEnergyHolder(event.getWorld());
			if (!(chunkEnergyHolder instanceof IChunkEnergyHolderModifiable)) return;

			((IChunkEnergyHolderModifiable) chunkEnergyHolder).removeChunkEnergy(event.getChunk().getPos());
		}
	}
}
