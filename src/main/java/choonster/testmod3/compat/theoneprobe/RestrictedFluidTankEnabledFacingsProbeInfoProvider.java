package choonster.testmod3.compat.theoneprobe;

import choonster.testmod3.block.RestrictedFluidTankBlock;
import choonster.testmod3.tileentity.RestrictedFluidTankTileEntity;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * Adds a line to the probe displaying the enabled facings of a {@link RestrictedFluidTankTileEntity}.
 *
 * @author Choonster
 */
public class RestrictedFluidTankEnabledFacingsProbeInfoProvider<BLOCK extends RestrictedFluidTankBlock> extends BaseProbeInfoProvider<BLOCK> {
	public RestrictedFluidTankEnabledFacingsProbeInfoProvider(
			final ResourceLocation id, final Class<BLOCK> blockClass
	) {
		super(id, blockClass);
	}

	@Override
	protected void addBlockProbeInfo(
			final ProbeMode mode, final IProbeInfo probeInfo, final PlayerEntity player,
			final World world, final BlockState blockState, final IProbeHitData data
	) {
		final BlockPos pos = data.getPos();
		final TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof RestrictedFluidTankTileEntity) {
			final String enabledFacingsString = ((RestrictedFluidTankBlock) blockState.getBlock())
					.getEnabledFacingsString(world, pos);

			probeInfo.text(new TranslationTextComponent(
					"block.testmod3.fluid_tank_restricted.enabled_facings",
					enabledFacingsString
			));
		}
	}

}
