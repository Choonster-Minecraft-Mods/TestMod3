package choonster.testmod3.capability.fluidhandler;

import choonster.testmod3.capability.CapabilityContainerListenerManager;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

/**
 * Not actually a capability added by this mod, just a central place to register things related to
 * {@link IFluidHandler}/{@link IFluidHandlerItem}.
 *
 * @author Choonster
 */
public final class FluidHandlerCapability {

	public static void register() {
		CapabilityContainerListenerManager.registerListenerFactory(FluidTankContainerListener::new);
	}
}
