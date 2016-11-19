package choonster.testmod3.compat.waila;

import choonster.testmod3.util.EnumFaceRotation;
import net.minecraft.block.properties.IProperty;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an {@link EnumFaceRotation} property.
 *
 * @author Choonster
 */
public class HUDHandlerMultiRotatable extends HUDHandlerEnumProperty<EnumFaceRotation> {
	public HUDHandlerMultiRotatable(IProperty<EnumFaceRotation> property) {
		super(property, "testmod3:multi_rotatable.face_rotation.desc", "testmod3:face_rotation");
	}
}
