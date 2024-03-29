package choonster.testmod3.compat.waila;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Property;
import snownee.jade.api.TooltipPosition;

/**
 * Adds a line to the Waila tooltip body displaying the current value of a {@link Direction} property.
 *
 * @author Choonster
 */
public class RotatableProvider extends EnumPropertyProvider<Direction> {
	public RotatableProvider(final ResourceLocation uid, final Property<Direction> property) {
		this(uid, property, TestMod3Lang.DESC_ROTATABLE_FACING.getTranslationKey());
	}

	public RotatableProvider(final ResourceLocation uid, final Property<Direction> property, final String tooltipTranslationKey) {
		super(
				uid,
				TooltipPosition.BODY,
				property,
				tooltipTranslationKey,
				TestMod3Lang.PREFIX_FACING.getTranslationKey()
		);
	}
}
