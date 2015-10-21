package com.choonster.testmod3.block.pipe;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A fluid pipe.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34448.0.html
 */
public class BlockPipeFluid extends BlockPipeBasic {
	public BlockPipeFluid() {
		setUnlocalizedName("fluidPipe");
	}

	@Override
	protected boolean isValidConnection(IBlockState ownState, IBlockState neighbourState, IBlockAccess world, BlockPos ownPos, EnumFacing neighbourDirection) {
		return super.isValidConnection(ownState, neighbourState, world, ownPos, neighbourDirection) || world.getTileEntity(ownPos.offset(neighbourDirection)) instanceof IFluidHandler;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}
}
