package choonster.testmod3.compat.waila;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.Property;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an enum property.
 *
 * @author Choonster
 */
public class EnumPropertyProvider<T extends Enum<T> & StringRepresentable> implements IBlockComponentProvider {
	protected final ResourceLocation uid;
	protected final int defaultPriority;
	protected final Property<T> property;
	protected final String tooltipTranslationKey;
	protected final String valueTranslationKeyPrefix;

	public EnumPropertyProvider(final ResourceLocation uid, final int defaultPriority, final Property<T> property, final String tooltipTranslationKey, final String valueTranslationKeyPrefix) {
		this.uid = uid;
		this.defaultPriority = defaultPriority;
		this.property = property;
		this.tooltipTranslationKey = tooltipTranslationKey;
		this.valueTranslationKeyPrefix = valueTranslationKeyPrefix;
	}

	@Override
	public ResourceLocation getUid() {
		return uid;
	}

	@Override
	public int getDefaultPriority() {
		return defaultPriority;
	}

	@Override
	public void appendTooltip(final ITooltip tooltip, final BlockAccessor accessor, final IPluginConfig config) {
		final var state = accessor.getBlockState();

		final var value = state.getValue(property);
		final var valueTranslationKey = valueTranslationKeyPrefix + "." + value.getSerializedName();

		tooltip.add(Component.translatable(tooltipTranslationKey, Component.translatable(valueTranslationKey)));
	}
}
