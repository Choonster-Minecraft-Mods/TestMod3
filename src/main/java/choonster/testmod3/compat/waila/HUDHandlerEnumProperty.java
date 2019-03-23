package choonster.testmod3.compat.waila;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.IProperty;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an enum property.
 *
 * @author Choonster
 */
public class HUDHandlerEnumProperty<T extends Enum<T> & IStringSerializable> implements IComponentProvider {
	protected final IProperty<T> property;
	protected final String tooltipTranslationKey;
	protected final String valueTranslationKeyPrefix;

	public HUDHandlerEnumProperty(final IProperty<T> property, final String tooltipTranslationKey, final String valueTranslationKeyPrefix) {
		this.property = property;
		this.tooltipTranslationKey = tooltipTranslationKey;
		this.valueTranslationKeyPrefix = valueTranslationKeyPrefix;
	}

	@Override
	public void appendBody(final List<ITextComponent> tooltip, final IDataAccessor accessor, final IPluginConfig config) {
		final IBlockState state = accessor.getBlockState();

		final T value = state.get(property);
		final String valueTranslationKey = valueTranslationKeyPrefix + "." + value.getName();

		tooltip.add(new TextComponentTranslation(tooltipTranslationKey, new TextComponentTranslation(valueTranslationKey)));
	}
}
