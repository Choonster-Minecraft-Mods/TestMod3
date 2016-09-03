package choonster.testmod3.capability.hiddenblockrevealer;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;

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
	public void setRevealHiddenBlocks(boolean revealHiddenBlocks) {
		this.revealHiddenBlocks = revealHiddenBlocks;
	}
}
