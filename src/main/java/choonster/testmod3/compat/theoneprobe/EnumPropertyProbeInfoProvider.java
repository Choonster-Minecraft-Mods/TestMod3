package choonster.testmod3.compat.theoneprobe;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.Property;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * Adds a line to the probe displaying the current value of an enum property.
 *
 * @author Choonster
 */
public class EnumPropertyProbeInfoProvider<BLOCK extends Block, ENUM extends Enum<ENUM> & IStringSerializable> extends BaseProbeInfoProvider<BLOCK> {
	protected final Property<ENUM> property;
	protected final String tooltipTranslationKey;
	protected final String valueTranslationKeyPrefix;

	public EnumPropertyProbeInfoProvider(
			final ResourceLocation id, final Class<BLOCK> blockClass, final Property<ENUM> property,
			final String tooltipTranslationKey, final String valueTranslationKeyPrefix
	) {
		super(id, blockClass);
		this.property = property;
		this.tooltipTranslationKey = tooltipTranslationKey;
		this.valueTranslationKeyPrefix = valueTranslationKeyPrefix;
	}

	@Override
	protected void addBlockProbeInfo(
			final ProbeMode mode, final IProbeInfo probeInfo, final PlayerEntity player,
			final World world, final BlockState blockState, final IProbeHitData data
	) {
		final ENUM value = blockState.getValue(property);
		final String valueTranslationKey = valueTranslationKeyPrefix + "." + value.getSerializedName();

		probeInfo.text(new TranslationTextComponent(tooltipTranslationKey, new TranslationTextComponent(valueTranslationKey)));
	}
}
