package choonster.testmod3.compat.waila;

import net.minecraft.state.Property;
import net.minecraft.util.Direction;

/**
 * Adds a line to the Waila tooltip body displaying the current value of a {@link Direction} property.
 *
 * @author Choonster
 */
public class RotatableHUDHandler extends EnumPropertyHUDHandler<Direction> {

	public RotatableHUDHandler(final Property<Direction> property) {
		this(property, "testmod3.rotatable.facing.desc");
	}

	public RotatableHUDHandler(final Property<Direction> property, final String tooltipTranslationKey) {
		super(property, tooltipTranslationKey, "testmod3.facing");
	}
}
