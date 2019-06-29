package choonster.testmod3.compat.waila;

import choonster.testmod3.block.*;
import mcp.mobius.waila.api.IRegistrar;
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

	@Override
	public void register(final IRegistrar registrar) {
		registrar.registerComponentProvider(new RotatableHUDHandler(ColoredRotatableBlock.FACING), TooltipPosition.BODY, ColoredRotatableBlock.class);
		registrar.registerComponentProvider(new MultiRotatableHUDHandler(ColoredMultiRotatableBlock.FACE_ROTATION), TooltipPosition.BODY, ColoredMultiRotatableBlock.class);

		registrar.registerComponentProvider(new RotatableHUDHandler(RotatableLampBlock.FACING), TooltipPosition.BODY, RotatableLampBlock.class);
		registrar.registerComponentProvider(new RotatableHUDHandler(ModChestBlock.FACING), TooltipPosition.BODY, ModChestBlock.class);

		registrar.registerComponentProvider(new RotatableHUDHandler(PlaneBlock.HORIZONTAL_ROTATION, "block.testmod3.plane.horizontal_rotation.desc"), TooltipPosition.BODY, PlaneBlock.class);
		registrar.registerComponentProvider(new VerticalRotatableHUDHandler(PlaneBlock.VERTICAL_ROTATION), TooltipPosition.BODY, PlaneBlock.class);

		registrar.registerComponentProvider(new RestrictedFluidTankEnabledFacingsHUDHandler(), TooltipPosition.BODY, RestrictedFluidTankBlock.class);
	}
}
