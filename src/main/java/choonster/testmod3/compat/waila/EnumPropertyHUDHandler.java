package choonster.testmod3.compat.waila;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an enum property.
 *
 * @author Choonster
 */
public class EnumPropertyHUDHandler<T extends Enum<T> & StringRepresentable> implements IComponentProvider {
	protected final Property<T> property;
	protected final String tooltipTranslationKey;
	protected final String valueTranslationKeyPrefix;

	public EnumPropertyHUDHandler(final Property<T> property, final String tooltipTranslationKey, final String valueTranslationKeyPrefix) {
		this.property = property;
		this.tooltipTranslationKey = tooltipTranslationKey;
		this.valueTranslationKeyPrefix = valueTranslationKeyPrefix;
	}

	@Override
	public void appendTooltip(final ITooltip tooltip, final BlockAccessor accessor, final IPluginConfig config) {
		final BlockState state = accessor.getBlockState();

		final T value = state.getValue(property);
		final String valueTranslationKey = valueTranslationKeyPrefix + "." + value.getSerializedName();

		tooltip.add(new TranslatableComponent(tooltipTranslationKey, new TranslatableComponent(valueTranslationKey)));
	}
}
