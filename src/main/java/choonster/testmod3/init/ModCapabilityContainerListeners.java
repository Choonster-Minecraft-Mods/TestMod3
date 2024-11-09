package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.capability.fluidhandler.FluidHandlerCapability;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Registers this mod's capability container listeners.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCapabilityContainerListeners {
	/**
	 * Register the capability container listeners.
	 *
	 * @param event The common setup event
	 */
	@SubscribeEvent
	public static void registerContainerListeners(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			FluidHandlerCapability.registerContainerListener();
		});
	}
}
