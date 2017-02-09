package choonster.testmod3.api.capability.lastusetime;

import javax.annotation.Nullable;

/**
 * A capability to track the last use time of something.
 *
 * @author Choonster
 */
public interface ILastUseTime {
	/**
	 * Get the last use time.
	 *
	 * @return The last use time
	 */
	long get();

	/**
	 * Set the last use time.
	 *
	 * @param lastUseTime The last use time
	 */
	void set(long lastUseTime);

	/**
	 * Should this object's last use time be updated automatically?
	 *
	 * @return Whether to receive automatic updates
	 */
	default boolean automaticUpdates() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementations must override {@link Object#equals(Object)} to perform a value comparison instead of a reference
	 * comparison.
	 */
	@Override
	boolean equals(@Nullable final Object obj);

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementations must override {@link Object#hashCode()} to generate a hash code based on the values used in
	 * {@link #equals(Object)}, as per the base method's contract.
	 */
	@Override
	int hashCode();
}
