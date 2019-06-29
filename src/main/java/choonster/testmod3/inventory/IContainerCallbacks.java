package choonster.testmod3.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;

/**
 * An interface to receive callbacks from a {@link Container}, similar to {@link IInventory}.
 *
 * @author Choonster
 */
public interface IContainerCallbacks {
	/**
	 * An instance of {@link IContainerCallbacks} that is always usable and does nothing when the {@link Container} is opened or closed.
	 */
	IContainerCallbacks NOOP = new IContainerCallbacks() {
		@Override
		public void onContainerOpened(final PlayerEntity player) {
			// No-op
		}

		@Override
		public void onContainerClosed(final PlayerEntity player) {
			// No-op
		}

		@Override
		public boolean isUsableByPlayer(final PlayerEntity player) {
			return true;
		}
	};

	/**
	 * Called when the {@link Container} is opened by a player.
	 *
	 * @param player The player
	 */
	void onContainerOpened(PlayerEntity player);

	/**
	 * Called when the {@link Container} is closed by a player.
	 *
	 * @param player The player
	 */
	void onContainerClosed(PlayerEntity player);

	/**
	 * Is this usable by the specified player?
	 *
	 * @param player The player
	 * @return Is this usable by the specified player?
	 */
	boolean isUsableByPlayer(PlayerEntity player);
}
