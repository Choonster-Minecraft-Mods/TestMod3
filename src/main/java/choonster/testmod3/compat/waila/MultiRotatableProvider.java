package choonster.testmod3.compat.waila;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.EnumFaceRotation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Property;
import snownee.jade.api.TooltipPosition;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an {@link EnumFaceRotation} property.
 *
 * @author Choonster
 */
public class MultiRotatableProvider extends EnumPropertyProvider<EnumFaceRotation> {
	public MultiRotatableProvider(final ResourceLocation uid, final Property<EnumFaceRotation> property) {
		super(
				uid,
				TooltipPosition.BODY,
				property,
				TestMod3Lang.DESC_MULTI_ROTATABLE_FACE_ROTATION.getTranslationKey(),
				TestMod3Lang.PREFIX_FACE_ROTATION.getTranslationKey()
		);
	}
}
