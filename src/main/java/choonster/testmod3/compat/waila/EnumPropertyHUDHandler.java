package choonster.testmod3.compat.waila;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an enum property.
 *
 * @author Choonster
 */
public class EnumPropertyHUDHandler<T extends Enum<T> & IStringSerializable> implements IComponentProvider {
	protected final Property<T> property;
	protected final String tooltipTranslationKey;
	protected final String valueTranslationKeyPrefix;

	public EnumPropertyHUDHandler(final Property<T> property, final String tooltipTranslationKey, final String valueTranslationKeyPrefix) {
		this.property = property;
		this.tooltipTranslationKey = tooltipTranslationKey;
		this.valueTranslationKeyPrefix = valueTranslationKeyPrefix;
	}

	@Override
	public void appendBody(final List<ITextComponent> tooltip, final IDataAccessor accessor, final IPluginConfig config) {
		final BlockState state = accessor.getBlockState();

		final T value = state.get(property);
		final String valueTranslationKey = valueTranslationKeyPrefix + "." + value.getString();

		tooltip.add(new TranslationTextComponent(tooltipTranslationKey, new TranslationTextComponent(valueTranslationKey)));
	}
}
