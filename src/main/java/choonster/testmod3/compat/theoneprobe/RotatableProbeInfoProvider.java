package choonster.testmod3.compat.theoneprobe;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * Adds a line to the probe displaying the current value of a {@link Direction} property.
 *
 * @author Choonster
 */
public class RotatableProbeInfoProvider<BLOCK extends Block> extends EnumPropertyProbeInfoProvider<BLOCK, Direction> {
	public RotatableProbeInfoProvider(
			final ResourceLocation id, final Class<BLOCK> blockClass, final Property<Direction> property
	) {
		this(id, blockClass, property, TestMod3Lang.DESC_ROTATABLE_FACING.getTranslationKey());
	}

	public RotatableProbeInfoProvider(
			final ResourceLocation id, final Class<BLOCK> blockClass,
			final Property<Direction> property, final String tooltipTranslationKey
	) {
		super(id, blockClass, property, tooltipTranslationKey, TestMod3Lang.PREFIX_FACING.getTranslationKey());
	}
}
