package choonster.testmod3.api.capability.lock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.INameable;
import net.minecraft.world.LockCode;

/**
 * A capability allowing things to be locked with a {@link LockCode} and only opened by players holding an item with a matching display name.
 * <p>
 * This is a copy of {@link net.minecraft.tileentity.LockableTileEntity} that doesn't extend {@link TileEntity} or implement {@link net.minecraft.inventory.IInventory}.
 *
 * @author Choonster
 */
public interface ILock extends INameable {

	/**
	 * @return Is this locked?
	 */
	boolean isLocked();

	/**
	 * Set the lock code.
	 *
	 * @param code The lock code
	 */
	void setLockCode(final LockCode code);

	/**
	 * Get the lock code.
	 *
	 * @return The lock code
	 */
	LockCode getLockCode();

	/**
	 * Try to open this lock, notifying the player if they can't.
	 *
	 * @param player The player opening the lock
	 * @return Was the player allowed to open the lock?
	 */
	default boolean tryOpen(final PlayerEntity player) {
		return LockableTileEntity.canUnlock(player, getLockCode(), getDisplayName());
	}
}
