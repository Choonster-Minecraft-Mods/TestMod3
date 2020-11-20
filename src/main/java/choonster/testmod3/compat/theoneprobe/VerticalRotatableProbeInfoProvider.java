package choonster.testmod3.compat.theoneprobe;

import choonster.testmod3.block.PlaneBlock;
import net.minecraft.block.Block;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;

/**
 * Adds a line to the probe displaying the current value of an {@link PlaneBlock.VerticalRotation} property.
 *
 * @author Choonster
 */
public class VerticalRotatableProbeInfoProvider<BLOCK extends Block> extends EnumPropertyProbeInfoProvider<BLOCK, PlaneBlock.VerticalRotation> {
	public VerticalRotatableProbeInfoProvider(
			final ResourceLocation id, final Class<BLOCK> blockClass,
			final Property<PlaneBlock.VerticalRotation> property
	) {
		super(
				id, blockClass, property,
				"block.testmod3.plane.vertical_rotation.desc",
				"testmod3.vertical_rotation"
		);
	}
}
