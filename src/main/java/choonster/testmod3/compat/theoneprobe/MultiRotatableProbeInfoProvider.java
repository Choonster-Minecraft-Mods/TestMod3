package choonster.testmod3.compat.theoneprobe;

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
				"testmod3.multi_rotatable.face_rotation.desc",
				"testmod3.face_rotation"
		);
	}
}
