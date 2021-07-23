package choonster.testmod3.capability.lock.wrapper;

import choonster.testmod3.api.capability.lock.ILock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.LockCode;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

/**
 * {@link ILock} wrapper around {@link LockableTileEntity}.
 *
 * @author Choonster
 */
public class LockableTileEntityWrapper implements ILock {
	private static final Field LOCK_KEY = ObfuscationReflectionHelper.findField(LockableTileEntity.class, /* lockKey */ "field_174901_a");

	private final LockableTileEntity lockableTileEntity;

	public LockableTileEntityWrapper(final LockableTileEntity lockableTileEntity) {
		this.lockableTileEntity = lockableTileEntity;
	}

	/**
	 * @return Is this locked?
	 */
	@Override
	public boolean isLocked() {
		// An empty ItemStack can only unlock an empty lock code
		return !getLockCode().unlocksWith(ItemStack.EMPTY);
	}

	/**
	 * Set the lock code.
	 *
	 * @param code The lock code
	 */
	@Override
	public void setLockCode(final LockCode code) {
		try {
			LOCK_KEY.set(lockableTileEntity, code);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(String.format("Couldn't set lock code of LockableTileEntity at %s", lockableTileEntity.getBlockPos()), e);
		}
	}

	/**
	 * Get the lock code.
	 *
	 * @return The lock code, if any
	 */
	@Override
	public LockCode getLockCode() {
		try {
			return (LockCode) LOCK_KEY.get(lockableTileEntity);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(String.format("Couldn't get lock code of LockableTileEntity at %s", lockableTileEntity.getBlockPos()), e);
		}
	}

	@Override
	public ITextComponent getDisplayName() {
		return lockableTileEntity.getDisplayName();
	}
}
