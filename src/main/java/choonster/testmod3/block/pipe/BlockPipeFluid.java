package choonster.testmod3.block.pipe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

/**
 * A fluid pipe.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34448.0.html
 *
 * @author Choonster
 */
public class BlockPipeFluid extends BlockPipeBase {
	public BlockPipeFluid(final Block.Properties properties) {
		super(properties);
	}

	@Override
	protected boolean isValidConnection(final IBlockState ownState, final IBlockState neighbourState, final IWorldReaderBase world, final BlockPos ownPos, final EnumFacing neighbourDirection) {
		// Connect if the neighbouring block is another pipe
		if (super.isValidConnection(ownState, neighbourState, world, ownPos, neighbourDirection)) {
			return true;
		}

		final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
		final Block neighbourBlock = neighbourState.getBlock();

		// Connect if the neighbouring block has a TileEntity with an IFluidHandler for the adjacent face
		if (neighbourBlock.hasTileEntity(neighbourState)) {
			final TileEntity tileEntity = world.getTileEntity(neighbourPos);
			return tileEntity != null && tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, neighbourDirection.getOpposite()).isPresent();
		}

		// Connect if the neighbouring block is a fluid, FluidUtil.getFluidHandler will provide an IFluidHandler wrapper to drain from it
		return neighbourBlock instanceof IFluidBlock || neighbourBlock instanceof BlockFlowingFluid;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
