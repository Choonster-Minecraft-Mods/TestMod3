package choonster.testmod3.compat.waila;

import net.minecraft.state.IProperty;
import net.minecraft.util.EnumFacing;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an {@link EnumFacing} property.
 *
 * @author Choonster
 */
public class HUDHandlerRotatable extends HUDHandlerEnumProperty<EnumFacing> {

	public HUDHandlerRotatable(final IProperty<EnumFacing> property) {
		this(property, "testmod3.rotatable.facing.desc");
	}

	public HUDHandlerRotatable(final IProperty<EnumFacing> property, final String tooltipTranslationKey) {
		super(property, tooltipTranslationKey, "testmod3.facing");
	}
}
