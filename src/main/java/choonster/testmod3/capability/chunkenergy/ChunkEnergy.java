package choonster.testmod3.capability.chunkenergy;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.network.UpdateChunkEnergyValueMessage;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.network.PacketDistributor;

/**
 * Default implementation of {@link IChunkEnergy}.
 *
 * @author Choonster
 */
public class ChunkEnergy extends EnergyStorage implements IChunkEnergy {
	/**
	 * The {@link Level} containing this instance's chunk.
	 */
	private final Level level;

	/**
	 * The {@link ChunkPos} of this instance's chunk.
	 */
	private final ChunkPos chunkPos;

	public ChunkEnergy(final int capacity, final Level level, final ChunkPos chunkPos) {
		super(capacity);
		this.level = level;
		this.chunkPos = chunkPos;
		energy = capacity;
	}

	@Override
	public Level getLevel() {
		return level;
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
		final Level level = getLevel();
		final ChunkPos chunkPos = getChunkPos();

		if (level.isClientSide) {
			return;
		}

		if (level.hasChunk(chunkPos.x, chunkPos.z)) {  // Don't load the chunk when reading from NBT
			final LevelChunk chunk = level.getChunk(chunkPos.x, chunkPos.z);
			chunk.setUnsaved(true);
			TestMod3.network.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new UpdateChunkEnergyValueMessage(this));
		}
	}
}
