package com.choonster.testmod3.capability.lastusetime;

import com.choonster.testmod3.api.capability.lastusetime.ILastUseTime;

/**
 * @author Choonster
 */
public class LastUseTime implements ILastUseTime {
	private long lastUseTime;

	/**
	 * Get the last use time.
	 *
	 * @return The last use time
	 */
	@Override
	public long get() {
		return lastUseTime;
	}

	/**
	 * Set the last use time.
	 *
	 * @param lastUseTime The last use time
	 */
	@Override
	public void set(long lastUseTime) {
		this.lastUseTime = lastUseTime;
	}
}
