package choonster.testmod3.compat.theoneprobe;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.EnumFaceRotation;
import net.minecraft.block.Block;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;

/**
 * Adds a line to the probe displaying the current value of an {@link EnumFaceRotation} property.
 *
 * @author Choonster
 */
public class MultiRotatableProbeInfoProvider<BLOCK extends Block> extends EnumPropertyProbeInfoProvider<BLOCK, EnumFaceRotation> {
	public MultiRotatableProbeInfoProvider(
			final ResourceLocation id, final Class<BLOCK> blockClass, final Property<EnumFaceRotation> property
	) {
		super(
				id, blockClass, property,
				TestMod3Lang.DESC_MULTI_ROTATABLE_FACE_ROTATION.getTranslationKey(),
				TestMod3Lang.PREFIX_FACE_ROTATION.getTranslationKey()
		);
	}
}
