package choonster.testmod3.api.capability.hiddenblockrevealer;

import javax.annotation.Nullable;

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
	void setRevealHiddenBlocks(final boolean revealHiddenBlocks);

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
