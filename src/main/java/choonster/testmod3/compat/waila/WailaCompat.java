package choonster.testmod3.compat.waila;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.world.level.block.*;
import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;

/**
 * Waila compatibility.
 *
 * @author Choonster
 */
@WailaPlugin
public class WailaCompat implements IWailaPlugin {
	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void registerClient(final IWailaClientRegistration registration) {
		registration.registerComponentProvider(new RotatableHUDHandler(ColoredRotatableBlock.FACING), TooltipPosition.BODY, ColoredRotatableBlock.class);
		registration.registerComponentProvider(new MultiRotatableHUDHandler(ColoredMultiRotatableBlock.FACE_ROTATION), TooltipPosition.BODY, ColoredMultiRotatableBlock.class);

		registration.registerComponentProvider(new RotatableHUDHandler(RotatableLampBlock.FACING), TooltipPosition.BODY, RotatableLampBlock.class);
		registration.registerComponentProvider(new RotatableHUDHandler(ModChestBlock.FACING), TooltipPosition.BODY, ModChestBlock.class);

		registration.registerComponentProvider(new RotatableHUDHandler(PlaneBlock.HORIZONTAL_ROTATION, TestMod3Lang.BLOCK_DESC_PLANE_HORIZONTAL_ROTATION.getTranslationKey()), TooltipPosition.BODY, PlaneBlock.class);
		registration.registerComponentProvider(new VerticalRotatableHUDHandler(PlaneBlock.VERTICAL_ROTATION), TooltipPosition.BODY, PlaneBlock.class);

		registration.registerComponentProvider(new RestrictedFluidTankEnabledFacingsHUDHandler(), TooltipPosition.BODY, RestrictedFluidTankBlock.class);
	}
}
