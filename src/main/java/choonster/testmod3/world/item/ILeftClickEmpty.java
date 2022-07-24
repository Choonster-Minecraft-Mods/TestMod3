package choonster.testmod3.world.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Implemented on items that need to handle being left-clicked on empty space.
 *
 * @author Choonster
 */
public interface ILeftClickEmpty {
	/**
	 * Called on both client and server when a player left-clicks empty space with this item.
	 *
	 * @param stack  The item being clicked with
	 * @param player The player
	 */
	void onLeftClickEmpty(ItemStack stack, Player player);
}
