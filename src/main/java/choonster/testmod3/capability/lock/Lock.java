package choonster.testmod3.capability.lock;

import choonster.testmod3.api.capability.lock.ILock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.LockCode;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

/**
 * Default implementation of {@link ILock}.
 *
 * @author Choonster
 */
public class Lock implements ILock, INBTSerializable<CompoundNBT> {
	/**
	 * The lock code.
	 */
	@Nonnull
	private LockCode code = LockCode.EMPTY_CODE;

	private final INameable nameProvider;

	public Lock(final INameable nameProvider) {
		this.nameProvider = nameProvider;
	}

	@Override
	public boolean isLocked() {
		// An empty ItemStack can only unlock an empty lock code
		return !getLockCode().func_219964_a(ItemStack.EMPTY);
	}

	@Override
	public LockCode getLockCode() {
		return code;
	}

	@Override
	public void setLockCode(final LockCode code) {
		this.code = code;
	}

	@Override
	public CompoundNBT serializeNBT() {
		final CompoundNBT tagCompound = new CompoundNBT();

		code.write(tagCompound);

		return tagCompound;
	}

	@Override
	public void deserializeNBT(final CompoundNBT nbt) {
		code = LockCode.read(nbt);
	}

	@Override
	public ITextComponent getDisplayName() {
		return nameProvider.getDisplayName();
	}
}
