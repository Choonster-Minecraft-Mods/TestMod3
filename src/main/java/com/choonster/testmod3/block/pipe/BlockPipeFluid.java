package com.choonster.testmod3.block.pipe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A fluid pipe.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34448.0.html
 *
 * @author Choonster
 */
public class BlockPipeFluid extends BlockPipeBasic {
	public BlockPipeFluid() {
		super("fluidPipe");
	}

	@Override
	protected boolean isValidConnection(IBlockState ownState, IBlockState neighbourState, IBlockAccess world, BlockPos ownPos, EnumFacing neighbourDirection) {
		// Connect if the neighbouring block is another pipe
		if (super.isValidConnection(ownState, neighbourState, world, ownPos, neighbourDirection)) {
			return true;
		}

		final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
		final Block neighbourBlock = neighbourState.getBlock();

		// Connect if the neighbouring block has a TileEntity with an IFluidHandler for the adjacent face
		if (neighbourBlock.hasTileEntity(neighbourState)) {
			final TileEntity tileEntity = world.getTileEntity(neighbourPos);
			return tileEntity != null && tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, neighbourDirection.getOpposite());
		}

		// Connect if the neighbouring block is a fluid/liquid, FluidUtil.getFluidHandler will provide an IFluidHandler wrapper to drain from it
		return neighbourBlock instanceof IFluidBlock || neighbourBlock instanceof BlockLiquid;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
