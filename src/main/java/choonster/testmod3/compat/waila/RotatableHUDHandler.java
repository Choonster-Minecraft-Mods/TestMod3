package choonster.testmod3.compat.waila;

import net.minecraft.state.IProperty;
import net.minecraft.util.Direction;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an {@link Direction} property.
 *
 * @author Choonster
 */
public class RotatableHUDHandler extends EnumPropertyHUDHandler<Direction> {

	public RotatableHUDHandler(final IProperty<Direction> property) {
		this(property, "testmod3.rotatable.facing.desc");
	}

	public RotatableHUDHandler(final IProperty<Direction> property, final String tooltipTranslationKey) {
		super(property, tooltipTranslationKey, "testmod3.facing");
	}
}
