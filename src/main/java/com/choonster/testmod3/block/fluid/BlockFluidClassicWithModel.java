package com.choonster.testmod3.block.fluid;

import com.choonster.testmod3.TestMod3;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import static com.choonster.testmod3.block.fluid.FluidUtils.*;

/**
 * The fluid rendering setup associated with this class was originally created by kirderf1 for www.github.com/mraof/minestuck
 * When copying this code, please keep this comment or refer back to the original source in another way, if possible.
 */
public class BlockFluidClassicWithModel extends BlockFluidClassic implements IFluidBlockWithModel {

	public BlockFluidClassicWithModel(Fluid fluid, Material material) {
		super(fluid, material);
		setUnlocalizedName(fluid.getUnlocalizedName());
		setCreativeTab(TestMod3.creativeTab);
	}

	@Override
	protected BlockState createBlockState() {
		return new ExtendedBlockState(this, new IProperty[]{LEVEL}, new IUnlistedProperty[]{HEIGHT_SW, HEIGHT_NW, HEIGHT_SE, HEIGHT_NE, FLOW_DIRECTION});
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		state = super.getExtendedState(state, world, pos);

		return FluidUtils.getExtendedState(this, state, world, pos);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {
		if (side == EnumFacing.UP)
			return world.getBlockState(pos).getBlock() != this;
		else return super.shouldSideBeRendered(world, pos, side);
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public int getDensityDir() {
		return densityDir;
	}


	// Fixed version of isSourceBlock that correctly compares this to the Block instead of the IBlockState
	@Override
	public boolean isSourceBlock(IBlockAccess world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() == this && (int) world.getBlockState(pos).getValue(LEVEL) == 0;
	}
}