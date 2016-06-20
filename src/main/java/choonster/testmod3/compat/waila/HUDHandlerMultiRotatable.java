package choonster.testmod3.compat.waila;

import choonster.testmod3.util.EnumFaceRotation;
import net.minecraft.block.properties.IProperty;

/**
 * @author Choonster
 */
public class HUDHandlerMultiRotatable extends HUDHandlerEnumProperty<EnumFaceRotation> {
	public HUDHandlerMultiRotatable(IProperty<EnumFaceRotation> property) {
		super(property, "testmod3:multiRotatable.faceRotation.desc", "testmod3:faceRotation");
	}
}
