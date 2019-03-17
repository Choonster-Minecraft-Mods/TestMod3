package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.capability.chunkenergy.CapabilityChunkEnergy;
import choonster.testmod3.capability.fluidhandler.CapabilityFluidHandler;
import choonster.testmod3.capability.hiddenblockrevealer.CapabilityHiddenBlockRevealer;
import choonster.testmod3.capability.lastusetime.CapabilityLastUseTime;
import choonster.testmod3.capability.lock.CapabilityLock;
import choonster.testmod3.capability.maxhealth.CapabilityMaxHealth;
import choonster.testmod3.capability.pigspawner.CapabilityPigSpawner;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Registers this mod's capabilities.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModCapabilities {
	/**
	 * Register the capabilities.
	 */
	@SubscribeEvent
	public static void registerCapabilities(final FMLCommonSetupEvent event) {
		CapabilityPigSpawner.register();
		CapabilityLastUseTime.register();
		CapabilityMaxHealth.register();
		CapabilityHiddenBlockRevealer.register();
		CapabilityLock.register();
		CapabilityChunkEnergy.register();
		CapabilityFluidHandler.register();
	}
}
