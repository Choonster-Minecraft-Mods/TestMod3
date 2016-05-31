package com.choonster.testmod3.compat.waila;

import com.choonster.testmod3.block.*;
import mcp.mobius.waila.api.IWailaRegistrar;

/**
 * @author Choonster
 */
public class WailaCompat {

	public static void register(IWailaRegistrar registrar) {
		registrar.registerStackProvider(HUDHandlerVariantTileEntityBlocks.INSTANCE, BlockColoredRotatable.class);
		registrar.registerBodyProvider(new HUDHandlerRotatable(BlockColoredRotatable.FACING), BlockColoredRotatable.class);
		registrar.registerBodyProvider(new HUDHandlerMultiRotatable(BlockColoredMultiRotatable.FACE_ROTATION), BlockColoredMultiRotatable.class);

		registrar.registerBodyProvider(new HUDHandlerRotatable(BlockRotatableLamp.FACING), BlockRotatableLamp.class);
		registrar.registerBodyProvider(new HUDHandlerRotatable(BlockModChest.FACING), BlockModChest.class);

		registrar.registerBodyProvider(new HUDHandlerRotatable(BlockPlane.HORIZONTAL_ROTATION, "tile.testmod3:plane.horizontalRotation.desc"), BlockPlane.class);
		registrar.registerBodyProvider(new HUDHandlerVerticalRotatable(BlockPlane.VERTICAL_ROTATION), BlockPlane.class);

	}
}
