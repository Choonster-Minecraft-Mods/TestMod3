package choonster.testmod3.compat.waila;

import choonster.testmod3.block.PlaneBlock;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.state.Property;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an {@link PlaneBlock.VerticalRotation} property.
 *
 * @author Choonster
 */
public class VerticalRotatableHUDHandler extends EnumPropertyHUDHandler<PlaneBlock.VerticalRotation> {
	public VerticalRotatableHUDHandler(final Property<PlaneBlock.VerticalRotation> property) {
		super(property, TestMod3Lang.BLOCK_DESC_PLANE_VERTICAL_ROTATION.getTranslationKey(), TestMod3Lang.PREFIX_VERTICAL_ROTATION.getTranslationKey());
	}
}
