package choonster.testmod3.compat.theoneprobe;

import choonster.testmod3.world.level.block.entity.RestrictedFluidTankBlockEntity;

/**
 * Adds a line to the probe displaying the enabled facings of a {@link RestrictedFluidTankBlockEntity}.
 *
 * @author Choonster
 */
/*
public class RestrictedFluidTankEnabledFacingsProbeInfoProvider<BLOCK extends RestrictedFluidTankBlock> extends BaseProbeInfoProvider<BLOCK> {
	public RestrictedFluidTankEnabledFacingsProbeInfoProvider(
			final ResourceLocation id, final Class<BLOCK> blockClass
	) {
		super(id, blockClass);
	}

	@Override
	protected void addBlockProbeInfo(
			final ProbeMode mode, final IProbeInfo probeInfo, final Player player,
			final Level world, final BlockState blockState, final IProbeHitData data
	) {
		final var pos = data.getPos();
		final var blockEntity = world.getBlockEntity(pos);

		if (blockEntity instanceof RestrictedFluidTankBlockEntity) {
			final var enabledFacingsString = ((RestrictedFluidTankBlock) blockState.getBlock())
					.getEnabledFacingsString(world, pos);

			probeInfo.text(Component.translatable(
					TestMod3Lang.BLOCK_DESC_FLUID_TANK_RESTRICTED_ENABLED_FACINGS.getTranslationKey(),
					enabledFacingsString
			));
		}
	}
}
*/
