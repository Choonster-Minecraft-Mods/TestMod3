package choonster.testmod3.compat.waila;

import choonster.testmod3.util.EnumFaceRotation;
import net.minecraft.state.IProperty;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an {@link EnumFaceRotation} property.
 *
 * @author Choonster
 */
public class MultiRotatableHUDHandler extends EnumPropertyHUDHandler<EnumFaceRotation> {
	public MultiRotatableHUDHandler(final IProperty<EnumFaceRotation> property) {
		super(property, "testmod3.multi_rotatable.face_rotation.desc", "testmod3.face_rotation");
	}
}
