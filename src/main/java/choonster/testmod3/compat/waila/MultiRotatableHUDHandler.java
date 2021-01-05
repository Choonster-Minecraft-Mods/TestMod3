package choonster.testmod3.compat.waila;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.EnumFaceRotation;
import net.minecraft.state.Property;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an {@link EnumFaceRotation} property.
 *
 * @author Choonster
 */
public class MultiRotatableHUDHandler extends EnumPropertyHUDHandler<EnumFaceRotation> {
	public MultiRotatableHUDHandler(final Property<EnumFaceRotation> property) {
		super(property, TestMod3Lang.DESC_MULTI_ROTATABLE_FACE_ROTATION.getTranslationKey(), TestMod3Lang.PREFIX_FACE_ROTATION.getTranslationKey());
	}
}
