package choonster.testmod3.compat.theoneprobe;

import net.minecraft.block.Block;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

/**
 * Adds a line to the probe displaying the current value of a {@link Direction} property.
 *
 * @author Choonster
 */
public class RotatableProbeInfoProvider<BLOCK extends Block> extends EnumPropertyProbeInfoProvider<BLOCK, Direction> {
	public RotatableProbeInfoProvider(
			final ResourceLocation id, final Class<BLOCK> blockClass, final Property<Direction> property
	) {
		this(id, blockClass, property, "testmod3.rotatable.facing.desc");
	}

	public RotatableProbeInfoProvider(
			final ResourceLocation id, final Class<BLOCK> blockClass,
			final Property<Direction> property, final String tooltipTranslationKey
	) {
		super(id, blockClass, property, tooltipTranslationKey, "testmod3.facing");
	}
}
