package choonster.testmod3.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

/**
 * An interface to receive callbacks from a {@link Container}, similar to {@link IInventory}.
 *
 * @author Choonster
 */
public interface IContainerCallbacks {
	/**
	 * Called when the {@link Container} is opened by a player.
	 *
	 * @param player The player
	 */
	void onContainerOpened(EntityPlayer player);

	/**
	 * Called when the {@link Container} is closed by a player.
	 *
	 * @param player The player
	 */
	void onContainerClosed(EntityPlayer player);

	/**
	 * Is this usable by the specified player?
	 *
	 * @param player The player
	 * @return Is this usable by the specified player?
	 */
	boolean isUsableByPlayer(EntityPlayer player);
}
