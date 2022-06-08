package choonster.testmod3.capability.hiddenblockrevealer;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import net.minecraft.nbt.ByteTag;
import net.minecraftforge.common.util.INBTSerializable;

import org.jetbrains.annotations.Nullable;

/**
 * Default implementation of {@link IHiddenBlockRevealer}
 *
 * @author Choonster
 */
public class HiddenBlockRevealer implements IHiddenBlockRevealer, INBTSerializable<ByteTag> {
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
	public ByteTag serializeNBT() {
		return ByteTag.valueOf(revealHiddenBlocks);
	}

	@Override
	public void deserializeNBT(final ByteTag tag) {
		revealHiddenBlocks = tag.getAsByte() != 0;
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
