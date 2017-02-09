package choonster.testmod3.capability.chunkenergy;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergyHolder;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergyHolderModifiable;
import net.minecraft.util.math.ChunkPos;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link IChunkEnergyHolder}.
 *
 * @author Choonster
 */
public class ChunkEnergyHolder implements IChunkEnergyHolderModifiable {
	private final Map<ChunkPos, IChunkEnergy> chunkEnergies = new HashMap<>();

	/**
	 * Get the {@link IChunkEnergy} for the specified chunk position.
	 *
	 * @param chunkPos The chunk position
	 * @return The IChunkEnergy, or null if there isn't one (e.g. the chunk isn't loaded)
	 */
	@Nullable
	@Override
	public IChunkEnergy getChunkEnergy(final ChunkPos chunkPos) {
		return chunkEnergies.get(chunkPos);
	}

	/**
	 * Set the {@link IChunkEnergy} for the specified chunk position.
	 *
	 * @param chunkPos    The chunk position
	 * @param chunkEnergy The IChunkEnergy
	 */
	@Override
	public void setChunkEnergy(final ChunkPos chunkPos, final IChunkEnergy chunkEnergy) {
		chunkEnergies.put(chunkPos, chunkEnergy);
	}

	/**
	 * Remove the {@link IChunkEnergy} for the specified chunk position.
	 *
	 * @param chunkPos The chunk position
	 */
	@Override
	public void removeChunkEnergy(final ChunkPos chunkPos) {
		chunkEnergies.remove(chunkPos);
	}
}
