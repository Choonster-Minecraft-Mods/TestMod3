package choonster.testmod3.compat.waila;

import choonster.testmod3.block.BlockPlane;
import net.minecraft.block.properties.IProperty;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an {@link BlockPlane.EnumVerticalRotation} property.
 *
 * @author Choonster
 */
public class HUDHandlerVerticalRotatable extends HUDHandlerEnumProperty<BlockPlane.EnumVerticalRotation> {
	public HUDHandlerVerticalRotatable(IProperty<BlockPlane.EnumVerticalRotation> property) {
		super(property, "tile.testmod3:plane.vertical_rotation.desc", "testmod3:vertical_rotation");
	}
}
