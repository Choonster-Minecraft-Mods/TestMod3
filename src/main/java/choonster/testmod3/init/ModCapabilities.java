package choonster.testmod3.init;

import choonster.testmod3.capability.lastusetime.CapabilityLastUseTime;
import choonster.testmod3.capability.maxhealth.CapabilityMaxHealth;
import choonster.testmod3.capability.pigspawner.CapabilityPigSpawner;

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
