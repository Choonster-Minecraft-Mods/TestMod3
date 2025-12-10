package choonster.testmod3.world.item.component.pigspawner;

import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

/**
 * A spawner that can only spawn a finite number of pigs.
 *
 * @author Choonster
 */
public interface IPigSpawnerFinite extends IPigSpawner {
	/**
	 * Get the current number of pigs that can be spawned.
	 *
	 * @return The number of pigs that can be spawned
	 */
	int numPigs();

	/**
	 * Get the maximum number of pigs that can be spawned.
	 *
	 * @return The maximum number of pigs that can be spawned.
	 */
	int maxNumPigs();

	/**
	 * Creates a copy of this spawner with a new number of pigs that can be spawned.
	 *
	 * @param numPigs The number of pigs that can be spawned
	 * @return The new spawner state
	 * @throws IllegalArgumentException If {@code numPigs} is greater than {@link #maxNumPigs()}
	 */
	IPigSpawnerFinite withNumPigs(final int numPigs);

	@Override
	@Nullable
	default IPigSpawnerFinite spawnPig(final Level level, final double x, final double y, final double z) {
		return (IPigSpawnerFinite) IPigSpawner.super.spawnPig(level, x, y, z);
	}
}
