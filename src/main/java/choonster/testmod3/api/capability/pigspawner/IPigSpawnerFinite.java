package choonster.testmod3.api.capability.pigspawner;

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
	int getNumPigs();

	/**
	 * Get the maximum number of pigs that can be spawned.
	 *
	 * @return The maximum number of pigs that can be spawned.
	 */
	int getMaxNumPigs();

	/**
	 * Set the current number of pigs that can be spawned.
	 *
	 * @param numPigs The number of pigs that can be spawned
	 * @throws IllegalArgumentException If {@code numPigs} is greater than {@link #getMaxNumPigs()}
	 */
	void setNumPigs(int numPigs);
}
