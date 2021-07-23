package choonster.testmod3.api.capability.chunkenergy;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Stores an energy value for a single chunk.
 *
 * @author Choonster
 */
public interface IChunkEnergy extends IEnergyStorage {

	/**
	 * Get the {@link Level} containing this instance's chunk.
	 *
	 * @return The Level
	 */
	Level getLevel();

	/**
	 * Get the {@link ChunkPos} of this instance's chunk.
	 *
	 * @return The chunk position
	 */
	ChunkPos getChunkPos();
}
