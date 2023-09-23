package choonster.testmod3.compat.waila;

/**
 * Adds a line to the Waila tooltip body displaying the current value of an enum property.
 *
 * @author Choonster
 */
/*
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
		final BlockState state = accessor.getBlockState();

		final T value = state.getValue(property);
		final String valueTranslationKey = valueTranslationKeyPrefix + "." + value.getSerializedName();

		tooltip.add(Component.translatable(tooltipTranslationKey, Component.translatable(valueTranslationKey)));
	}
}
*/
