package choonster.testmod3.compat.waila;

import choonster.testmod3.TestMod3;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.world.level.block.PlaneBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Property;
import snownee.jade.api.TooltipPosition;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an {@link PlaneBlock.VerticalRotation} property.
 *
 * @author Choonster
 */
public class VerticalRotatableHUDHandler extends EnumPropertyHUDHandler<PlaneBlock.VerticalRotation> {
	public VerticalRotatableHUDHandler(final Property<PlaneBlock.VerticalRotation> property) {
		super(
				new ResourceLocation(TestMod3.MODID, "plane_vertical_rotation"),
				TooltipPosition.BODY,
				property,
				TestMod3Lang.BLOCK_DESC_PLANE_VERTICAL_ROTATION.getTranslationKey(),
				TestMod3Lang.PREFIX_VERTICAL_ROTATION.getTranslationKey()
		);
	}
}
