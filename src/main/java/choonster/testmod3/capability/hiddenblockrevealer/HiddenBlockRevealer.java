package choonster.testmod3.capability.hiddenblockrevealer;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;

import javax.annotation.Nullable;

/**
 * Default implementation of {@link IHiddenBlockRevealer}
 *
 * @author Choonster
 */
public class HiddenBlockRevealer implements IHiddenBlockRevealer {
	/**
	 * Should hidden blocks be revealed?
	 */
	private boolean revealHiddenBlocks;

	/**
	 * @return Should hidden blocks be revealed?
	 */
	@Override
	public boolean revealHiddenBlocks() {
		return revealHiddenBlocks;
	}

	/**
	 * @param revealHiddenBlocks Should hidden blocks be revealed?
	 */
	@Override
	public void setRevealHiddenBlocks(final boolean revealHiddenBlocks) {
		this.revealHiddenBlocks = revealHiddenBlocks;
	}


	@Override
	public boolean equals(@Nullable final Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		final HiddenBlockRevealer that = (HiddenBlockRevealer) obj;

		return revealHiddenBlocks == that.revealHiddenBlocks;
	}

	@Override
	public int hashCode() {
		return revealHiddenBlocks ? 1 : 0;
	}
}
