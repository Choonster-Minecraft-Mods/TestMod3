package com.choonster.testmod3.init;

import com.choonster.testmod3.capability.lastusetime.CapabilityLastUseTime;
import com.choonster.testmod3.capability.maxhealth.CapabilityMaxHealth;
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
		CapabilityLastUseTime.register();
		CapabilityMaxHealth.register();
	}
}
