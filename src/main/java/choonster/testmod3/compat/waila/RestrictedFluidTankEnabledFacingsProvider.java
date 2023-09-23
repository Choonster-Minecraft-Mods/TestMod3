package choonster.testmod3.compat.waila;

import choonster.testmod3.world.level.block.entity.RestrictedFluidTankBlockEntity;

/**
 * Adds a line to the Waila tooltip body displaying the enabled facings of a {@link RestrictedFluidTankBlockEntity}.
 *
 * @author Choonster
 */
/*
public class RestrictedFluidTankEnabledFacingsProvider implements IBlockComponentProvider {
	private final ResourceLocation uid;

	public RestrictedFluidTankEnabledFacingsProvider(final ResourceLocation uid) {
		this.uid = uid;
	}

	@Override
	public ResourceLocation getUid() {
		return uid;
	}

	@Override
	public int getDefaultPriority() {
		return TooltipPosition.BODY;
	}

	@Override
	public void appendTooltip(final ITooltip tooltip, final BlockAccessor accessor, final IPluginConfig config) {
		final var blockEntity = accessor.getBlockEntity();

		if (blockEntity instanceof RestrictedFluidTankBlockEntity) {
			final var enabledFacingsString = ((RestrictedFluidTankBlock) accessor.getBlock())
					.getEnabledFacingsString(accessor.getLevel(), accessor.getPosition());

			tooltip.add(Component.translatable(TestMod3Lang.BLOCK_DESC_FLUID_TANK_RESTRICTED_ENABLED_FACINGS.getTranslationKey(), enabledFacingsString));
		}
	}
}
*/
