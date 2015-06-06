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
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;

import static com.choonster.testmod3.block.fluid.FluidUtils.*;

public class BlockFluidFiniteWithModel extends BlockFluidFinite implements IFluidBlockWithModel {
	public BlockFluidFiniteWithModel(Fluid fluid, Material material) {
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
}
