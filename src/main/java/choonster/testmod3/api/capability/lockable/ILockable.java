package choonster.testmod3.api.capability.lockable;

import net.minecraft.world.LockCode;

import javax.annotation.Nullable;

/**
 * A copy of {@link net.minecraft.world.ILockableContainer} that doesn't implement {@link net.minecraft.inventory.IInventory}.
 *
 * @author Choonster
 */
public interface ILockable {
	boolean isLocked();

	void setLockCode(LockCode code);

	@Nullable
	LockCode getLockCode();
}
