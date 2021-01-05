package choonster.testmod3.compat.theoneprobe;

import choonster.testmod3.block.PlaneBlock;
import choonster.testmod3.text.TestMod3Lang;
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
				TestMod3Lang.BLOCK_DESC_PLANE_VERTICAL_ROTATION.getTranslationKey(),
				TestMod3Lang.PREFIX_VERTICAL_ROTATION.getTranslationKey()
		);
	}
}
