package choonster.testmod3.compat.waila;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;

/**
 * @author Choonster
 */
public class HUDHandlerRotatable extends HUDHandlerEnumProperty<EnumFacing> {

	public HUDHandlerRotatable(IProperty<EnumFacing> property) {
		this(property, "testmod3:rotatable.facing.desc");
	}

	public HUDHandlerRotatable(IProperty<EnumFacing> property, String tooltipTranslationKey) {
		super(property, tooltipTranslationKey, "testmod3:facing");
	}
}
