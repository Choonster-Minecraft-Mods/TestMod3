package choonster.testmod3.api.capability.chunkenergy;

import net.minecraft.util.math.ChunkPos;

/**
 * Allows {@link IChunkEnergy} instances to be added and removed.
 *
 * @author Choonster
 */
public interface IChunkEnergyHolderModifiable extends IChunkEnergyHolder {

	/**
	 * Set the {@link IChunkEnergy} for the specified chunk position.
	 *
	 * @param chunkPos           The chunk position
	 * @param chunkEnergy The IChunkEnergy
	 */
	void setChunkEnergy(ChunkPos chunkPos, IChunkEnergy chunkEnergy);

	/**
	 * Remove the {@link IChunkEnergy} for the specified chunk position.
	 *
	 * @param chunkPos The chunk position
	 */
	void removeChunkEnergy(ChunkPos chunkPos);
}
