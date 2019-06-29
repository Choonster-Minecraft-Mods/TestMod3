package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import choonster.testmod3.capability.fluidhandler.FluidHandlerCapability;
import choonster.testmod3.capability.hiddenblockrevealer.HiddenBlockRevealerCapability;
import choonster.testmod3.capability.lastusetime.LastUseTimeCapability;
import choonster.testmod3.capability.lock.LockCapability;
import choonster.testmod3.capability.maxhealth.MaxHealthCapability;
import choonster.testmod3.capability.pigspawner.PigSpawnerCapability;
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
		PigSpawnerCapability.register();
		LastUseTimeCapability.register();
		MaxHealthCapability.register();
		HiddenBlockRevealerCapability.register();
		LockCapability.register();
		ChunkEnergyCapability.register();
		FluidHandlerCapability.register();
	}
}
