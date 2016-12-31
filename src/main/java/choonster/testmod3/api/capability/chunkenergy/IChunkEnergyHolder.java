package choonster.testmod3.api.capability.chunkenergy;

import net.minecraft.util.math.ChunkPos;

import javax.annotation.Nullable;

/**
 * A capability to hold per-chunk {@link IChunkEnergy} instances.
 *
 * @author Choonster
 */
public interface IChunkEnergyHolder {

	/**
	 * Get the {@link IChunkEnergy} for the specified chunk position.
	 *
	 * @param chunkPos The chunk position
	 * @return The IChunkEnergy, or null if the chunk isn't loaded
	 */
	@Nullable
	IChunkEnergy getChunkEnergy(ChunkPos chunkPos);
}
