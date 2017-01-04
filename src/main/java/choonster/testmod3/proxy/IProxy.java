package choonster.testmod3.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IProxy {
	void preInit();

	void init();

	void postInit();

	/**
	 * Perform a right click on the client side.
	 */
	void doClientRightClick();

	/**
	 * Get the client player if on the client, or null if on the dedicated server.
	 *
	 * @return The client player
	 */
	@Nullable
	EntityPlayer getClientPlayer();

	/**
	 * Get the client {@link World} if on the client, or null if on the dedicated server.
	 *
	 * @return The client World
	 */
	@Nullable
	World getClientWorld();
}
