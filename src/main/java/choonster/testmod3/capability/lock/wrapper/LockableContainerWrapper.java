package choonster.testmod3.capability.lock.wrapper;

import choonster.testmod3.api.capability.lock.ILock;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

/**
 * {@link ILock} wrapper around {@link ILockableContainer}.
 *
 * @author Choonster
 */
public class LockableContainerWrapper implements ILock {
	private final ILockableContainer lockableContainer;

	public LockableContainerWrapper(ILockableContainer lockableContainer) {
		this.lockableContainer = lockableContainer;
	}

	/**
	 * @return Is this locked?
	 */
	@Override
	public boolean isLocked() {
		return lockableContainer.isLocked();
	}

	/**
	 * Set the lock code.
	 *
	 * @param code The lock code
	 */
	@Override
	public void setLockCode(LockCode code) {
		lockableContainer.setLockCode(code);
	}

	/**
	 * Get the lock code.
	 *
	 * @return The lock code, if any
	 */
	@Override
	public LockCode getLockCode() {
		final LockCode lockCode = lockableContainer.getLockCode();
		return lockCode != null ? lockCode : LockCode.EMPTY_CODE;
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	@Override
	public String getName() {
		return lockableContainer.getName();
	}

	/**
	 * Returns true if this thing is named
	 */
	@Override
	public boolean hasCustomName() {
		return lockableContainer.hasCustomName();
	}

	/**
	 * Get the formatted ChatComponent that will be used for the sender's username in chat
	 */
	@Override
	public ITextComponent getDisplayName() {
		return lockableContainer.getDisplayName();
	}
}
