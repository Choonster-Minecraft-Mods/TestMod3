package choonster.testmod3.compat.theoneprobe;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * Adds a line to the probe displaying the current value of an enum property.
 *
 * @author Choonster
 */
public class EnumPropertyProbeInfoProvider<BLOCK extends Block, ENUM extends Enum<ENUM> & StringRepresentable> extends BaseProbeInfoProvider<BLOCK> {
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
			final ProbeMode mode, final IProbeInfo probeInfo, final Player player,
			final Level world, final BlockState blockState, final IProbeHitData data
	) {
		final ENUM value = blockState.getValue(property);
		final String valueTranslationKey = valueTranslationKeyPrefix + "." + value.getSerializedName();

		probeInfo.text(new TranslatableComponent(tooltipTranslationKey, new TranslatableComponent(valueTranslationKey)));
	}
}
