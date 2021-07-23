package choonster.testmod3.api.capability.lock;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * A capability allowing things to be locked with a {@link LockCode} and only opened by players holding an item with a matching display name.
 * <p>
 * This is a copy of {@link BaseContainerBlockEntity} that doesn't extend {@link BlockEntity} or implement {@link Container}.
 *
 * @author Choonster
 */
public interface ILock {

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
	 * Get the display name.
	 *
	 * @return The display name.
	 */
	Component getDisplayName();

	/**
	 * Try to open this lock, notifying the player if they can't.
	 *
	 * @param player The player opening the lock
	 * @return Was the player allowed to open the lock?
	 */
	default boolean tryOpen(final Player player) {
		return BaseContainerBlockEntity.canUnlock(player, getLockCode(), getDisplayName());
	}
}
