package com.choonster.testmod3.init;

import com.choonster.testmod3.capability.pigspawner.CapabilityPigSpawner;

/**
 * Registers this mods capabilities.
 *
 * @author Choonster
 */
public class ModCapabilities {

	/**
	 * Register the capabilities.
	 */
	public static void registerCapabilities() {
		CapabilityPigSpawner.register();
	}
}
