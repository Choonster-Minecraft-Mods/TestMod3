package choonster.testmod3.compat.waila;

import choonster.testmod3.block.BlockPlane;
import net.minecraft.block.properties.IProperty;

/**
 * @author Choonster
 */
public class HUDHandlerVerticalRotatable extends HUDHandlerEnumProperty<BlockPlane.EnumVerticalRotation> {
	public HUDHandlerVerticalRotatable(IProperty<BlockPlane.EnumVerticalRotation> property) {
		super(property, "tile.testmod3:plane.verticalRotation.desc", "testmod3:verticalRotation");
	}
}
