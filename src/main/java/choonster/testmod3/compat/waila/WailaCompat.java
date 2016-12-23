package choonster.testmod3.compat.waila;

import choonster.testmod3.block.*;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

/**
 * Waila compatibility.
 *
 * @author Choonster
 */
@WailaPlugin
public class WailaCompat implements IWailaPlugin {

	public void register(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(new HUDHandlerRotatable(BlockColoredRotatable.FACING), BlockColoredRotatable.class);
		registrar.registerBodyProvider(new HUDHandlerMultiRotatable(BlockColoredMultiRotatable.FACE_ROTATION), BlockColoredMultiRotatable.class);

		registrar.registerBodyProvider(new HUDHandlerRotatable(BlockRotatableLamp.FACING), BlockRotatableLamp.class);
		registrar.registerBodyProvider(new HUDHandlerRotatable(BlockModChest.FACING), BlockModChest.class);

		registrar.registerBodyProvider(new HUDHandlerRotatable(BlockPlane.HORIZONTAL_ROTATION, "tile.testmod3:plane.horizontal_rotation.desc"), BlockPlane.class);
		registrar.registerBodyProvider(new HUDHandlerVerticalRotatable(BlockPlane.VERTICAL_ROTATION), BlockPlane.class);
	}
}
