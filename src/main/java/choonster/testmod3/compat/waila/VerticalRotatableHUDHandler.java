package choonster.testmod3.compat.waila;

import choonster.testmod3.block.PlaneBlock;
import net.minecraft.state.IProperty;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an {@link PlaneBlock.VerticalRotation} property.
 *
 * @author Choonster
 */
public class VerticalRotatableHUDHandler extends EnumPropertyHUDHandler<PlaneBlock.VerticalRotation> {
	public VerticalRotatableHUDHandler(final IProperty<PlaneBlock.VerticalRotation> property) {
		super(property, "block.testmod3.plane.vertical_rotation.desc", "testmod3.vertical_rotation");
	}
}
