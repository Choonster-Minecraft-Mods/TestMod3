package com.choonster.testmod3.block.pipe;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.IFluidHandler;
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
		return super.isValidConnection(ownState, neighbourState, world, ownPos, neighbourDirection) || world.getTileEntity(ownPos.offset(neighbourDirection)) instanceof IFluidHandler;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
