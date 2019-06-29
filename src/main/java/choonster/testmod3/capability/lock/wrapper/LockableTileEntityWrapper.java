package choonster.testmod3.capability.lock.wrapper;

import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.util.ReflectionUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.LockCode;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * {@link ILock} wrapper around {@link LockableTileEntity}.
 *
 * @author Choonster
 */
public class LockableTileEntityWrapper implements ILock {
	private static final Field CODE = ReflectionUtil.findField(LockableTileEntity.class, "field_174901_a" /* code */);

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
		return !getLockCode().func_219964_a(ItemStack.EMPTY);
	}

	/**
	 * Set the lock code.
	 *
	 * @param code The lock code
	 */
	@Override
	public void setLockCode(final LockCode code) {
		try {
			CODE.set(lockableTileEntity, code);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(String.format("Couldn't set lock code of LockableTileEntity at %s", lockableTileEntity.getPos()), e);
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
			return (LockCode) CODE.get(lockableTileEntity);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(String.format("Couldn't get lock code of LockableTileEntity at %s", lockableTileEntity.getPos()), e);
		}
	}


	@Override
	public ITextComponent getName() {
		return lockableTileEntity.getName();
	}

	/**
	 * Returns true if this thing is named
	 */
	@Override
	public boolean hasCustomName() {
		return lockableTileEntity.hasCustomName();
	}

	/**
	 * Get the formatted ITextComponent that will be used for the sender's username in chat
	 */
	@Override
	public ITextComponent getDisplayName() {
		return lockableTileEntity.getDisplayName();
	}

	@Nullable
	@Override
	public ITextComponent getCustomName() {
		return lockableTileEntity.getCustomName();
	}
}
