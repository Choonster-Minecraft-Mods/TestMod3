package choonster.testmod3.api.capability.hiddenblockrevealer;

/**
 * A capability to reveal hidden blocks.
 *
 * @author Choonster
 */
public interface IHiddenBlockRevealer {

	/**
	 * @return Should hidden blocks be revealed?
	 */
	boolean revealHiddenBlocks();

	/**
	 * @param revealHiddenBlocks Should hidden blocks be revealed?
	 */
	void setRevealHiddenBlocks(boolean revealHiddenBlocks);
}
