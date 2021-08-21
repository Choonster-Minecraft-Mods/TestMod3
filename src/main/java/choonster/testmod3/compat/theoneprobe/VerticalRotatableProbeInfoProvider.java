package choonster.testmod3.compat.theoneprobe;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.world.level.block.PlaneBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

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
