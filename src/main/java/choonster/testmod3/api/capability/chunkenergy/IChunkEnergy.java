package choonster.testmod3.api.capability.chunkenergy;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Stores an energy value for a single chunk.
 *
 * @author Choonster
 */
public interface IChunkEnergy extends IEnergyStorage {

	/**
	 * Get the {@link World} containing this instance's chunk.
	 *
	 * @return The World
	 */
	World getWorld();

	/**
	 * Get the {@link ChunkPos} of this instance's chunk.
	 *
	 * @return The chunk position
	 */
	ChunkPos getChunkPos();
}
