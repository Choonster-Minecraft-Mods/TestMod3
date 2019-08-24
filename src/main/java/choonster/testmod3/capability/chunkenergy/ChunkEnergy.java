package choonster.testmod3.capability.chunkenergy;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.network.UpdateChunkEnergyValueMessage;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * Default implementation of {@link IChunkEnergy}.
 *
 * @author Choonster
 */
public class ChunkEnergy extends EnergyStorage implements IChunkEnergy, INBTSerializable<IntNBT> {
	/**
	 * The {@link World} containing this instance's chunk.
	 */
	private final World world;

	/**
	 * The {@link ChunkPos} of this instance's chunk.
	 */
	private final ChunkPos chunkPos;

	public ChunkEnergy(final int capacity, final World world, final ChunkPos chunkPos) {
		super(capacity);
		this.world = world;
		this.chunkPos = chunkPos;
		energy = capacity;
	}

	@Override
	public IntNBT serializeNBT() {
		return new IntNBT(getEnergyStored());
	}

	@Override
	public void deserializeNBT(final IntNBT nbt) {
		energy = nbt.getInt();
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public ChunkPos getChunkPos() {
		return chunkPos;
	}

	@Override
	public int receiveEnergy(final int maxReceive, final boolean simulate) {
		final int energyReceived = super.receiveEnergy(maxReceive, simulate);

		if (!simulate && energyReceived != 0) {
			onEnergyChanged();
		}

		return energyReceived;
	}

	@Override
	public int extractEnergy(final int maxExtract, final boolean simulate) {
		final int energyExtracted = super.extractEnergy(maxExtract, simulate);

		if (!simulate && energyExtracted != 0) {
			onEnergyChanged();
		}

		return energyExtracted;
	}

	/**
	 * Set the energy value. For internal use only.
	 *
	 * @param energy The new energy value
	 */
	public void setEnergy(final int energy) {
		this.energy = energy;
		onEnergyChanged();
	}

	/**
	 * Called when the energy value changes.
	 */
	protected void onEnergyChanged() {
		final World world = getWorld();
		final ChunkPos chunkPos = getChunkPos();

		if (world.isRemote) return;

		if (world.getChunkProvider().isChunkLoaded(chunkPos)) {  // Don't load the chunk when reading from NBT
			final Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
			chunk.markDirty();
			TestMod3.network.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new UpdateChunkEnergyValueMessage(this));
		}
	}
}
