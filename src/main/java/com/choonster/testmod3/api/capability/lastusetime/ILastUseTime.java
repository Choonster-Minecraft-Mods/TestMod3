package com.choonster.testmod3.api.capability.lastusetime;

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
}
