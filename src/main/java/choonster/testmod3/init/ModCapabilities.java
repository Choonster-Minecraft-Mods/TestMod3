package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import choonster.testmod3.capability.fluidhandler.FluidHandlerCapability;
import choonster.testmod3.capability.hiddenblockrevealer.HiddenBlockRevealerCapability;
import choonster.testmod3.capability.lastusetime.LastUseTimeCapability;
import choonster.testmod3.capability.lock.LockCapability;
import choonster.testmod3.capability.maxhealth.MaxHealthCapability;
import choonster.testmod3.capability.pigspawner.PigSpawnerCapability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

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
	public static void registerCapabilities(final RegisterCapabilitiesEvent event) {
		PigSpawnerCapability.register(event);
		LastUseTimeCapability.register(event);
		MaxHealthCapability.register(event);
		HiddenBlockRevealerCapability.register(event);
		LockCapability.register(event);
		ChunkEnergyCapability.register(event);
		FluidHandlerCapability.register();
	}
}
