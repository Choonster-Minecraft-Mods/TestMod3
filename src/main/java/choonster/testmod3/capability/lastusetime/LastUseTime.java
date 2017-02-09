package choonster.testmod3.capability.lastusetime;

import choonster.testmod3.api.capability.lastusetime.ILastUseTime;

import javax.annotation.Nullable;

/**
 * Default implementation of {@link ILastUseTime}.
 *
 * @author Choonster
 */
public class LastUseTime implements ILastUseTime {
	private long lastUseTime;
	private final boolean automaticUpdates;

	public LastUseTime(boolean automaticUpdates) {
		this.automaticUpdates = automaticUpdates;
	}

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

	/**
	 * Should this object's last use time be updated automatically?
	 *
	 * @return Whether to receive automatic updates
	 */
	@Override
	public boolean automaticUpdates() {
		return automaticUpdates;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		final LastUseTime that = (LastUseTime) obj;

		return lastUseTime == that.lastUseTime;
	}

	@Override
	public int hashCode() {
		return (int) (lastUseTime ^ (lastUseTime >>> 32));
	}
}
