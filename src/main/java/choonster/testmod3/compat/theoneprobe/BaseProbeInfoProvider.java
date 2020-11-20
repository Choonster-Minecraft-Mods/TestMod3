package choonster.testmod3.compat.theoneprobe;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Base class for this mod's {@link IProbeInfoProvider}s.
 *
 * @author Choonster
 */
public abstract class BaseProbeInfoProvider<BLOCK extends Block> implements IProbeInfoProvider {
	protected final ResourceLocation id;
	protected final Class<BLOCK> blockClass;

	public BaseProbeInfoProvider(final ResourceLocation id, final Class<BLOCK> blockClass) {
		this.id = id;
		this.blockClass = blockClass;
	}

	@Override
	public String getID() {
		return id.toString();
	}

	@Override
	public final void addProbeInfo(
			final ProbeMode mode, final IProbeInfo probeInfo, final PlayerEntity player,
			final World world, final BlockState blockState, final IProbeHitData data
	) {
		if (blockClass.isInstance(blockState.getBlock())) {
			addBlockProbeInfo(mode, probeInfo, player, world, blockState, data);
		}
	}

	/**
	 * Add probe information for the block. This is only called for blocks matching the class specified in the constructor.
	 */
	protected abstract void addBlockProbeInfo(
			final ProbeMode mode, final IProbeInfo probeInfo, final PlayerEntity player,
			final World world, final BlockState blockState, final IProbeHitData data
	);
}
