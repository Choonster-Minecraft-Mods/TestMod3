package choonster.testmod3.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * An interface to receive callbacks from an {@link AbstractContainerMenu}, similar to {@link Container}.
 *
 * @author Choonster
 */
public interface IMenuCallbacks {
	/**
	 * An instance of {@link IMenuCallbacks} that is always usable and does nothing when the {@link AbstractContainerMenu} is opened or closed.
	 */
	IMenuCallbacks NOOP = new IMenuCallbacks() {
		@Override
		public void onMenuOpened(final Player player) {
			// No-op
		}

		@Override
		public void onMenuClosed(final Player player) {
			// No-op
		}

		@Override
		public boolean isUsableByPlayer(final Player player) {
			return true;
		}
	};

	/**
	 * Called when the {@link AbstractContainerMenu} is opened by a player.
	 *
	 * @param player The player
	 */
	void onMenuOpened(Player player);

	/**
	 * Called when the {@link AbstractContainerMenu} is closed by a player.
	 *
	 * @param player The player
	 */
	void onMenuClosed(Player player);

	/**
	 * Is this usable by the specified player?
	 *
	 * @param player The player
	 * @return Is this usable by the specified player?
	 */
	boolean isUsableByPlayer(Player player);
}
