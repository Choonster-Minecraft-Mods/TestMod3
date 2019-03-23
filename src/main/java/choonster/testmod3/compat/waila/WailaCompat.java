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
		registrar.registerComponentProvider(new HUDHandlerRotatable(BlockColoredRotatable.FACING), TooltipPosition.BODY, BlockColoredRotatable.class);
		registrar.registerComponentProvider(new HUDHandlerMultiRotatable(BlockColoredMultiRotatable.FACE_ROTATION), TooltipPosition.BODY, BlockColoredMultiRotatable.class);

		registrar.registerComponentProvider(new HUDHandlerRotatable(BlockRotatableLamp.FACING), TooltipPosition.BODY, BlockRotatableLamp.class);
		registrar.registerComponentProvider(new HUDHandlerRotatable(BlockModChest.FACING), TooltipPosition.BODY, BlockModChest.class);

		registrar.registerComponentProvider(new HUDHandlerRotatable(BlockPlane.HORIZONTAL_ROTATION, "tile.testmod3:plane.horizontal_rotation.desc"), TooltipPosition.BODY, BlockPlane.class);
		registrar.registerComponentProvider(new HUDHandlerVerticalRotatable(BlockPlane.VERTICAL_ROTATION), TooltipPosition.BODY, BlockPlane.class);

		registrar.registerComponentProvider(new HUDHandlerFluidTankRestrictedEnabledFacings(), TooltipPosition.BODY, BlockFluidTankRestricted.class);
	}
}
