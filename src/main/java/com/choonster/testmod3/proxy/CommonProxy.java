package com.choonster.testmod3.proxy;

import net.minecraft.entity.player.EntityPlayer;

public abstract class CommonProxy {
	public void preInit() {

	}

	public void init() {

	}

	public void postInit() {

	}

	/**
	 * Perform a right click on the client side.
	 */
	public void doClientRightClick() {

	}

	/**
	 * Get the client player if on the client, or null if on the dedicated server.
	 *
	 * @return The client player
	 */
	public EntityPlayer getClientPlayer() {
		return null;
	}
}
