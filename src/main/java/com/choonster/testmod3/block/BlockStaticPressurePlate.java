package com.choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * A block that uses pressure plate model, placement and piston movement behaviour; but doesn't depress when stood on.
 *
 * @author Choonster
 */
public abstract class BlockStaticPressurePlate extends BlockTestMod3 {
	protected final AxisAlignedBB BB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.03125D, 0.9375D);

	public BlockStaticPressurePlate(Material materialIn, String blockName) {
		super(materialIn, blockName);
		fullBlock = false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return canBePlacedOn(worldIn, pos.down());
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (!this.canBePlacedOn(worldIn, pos.down())) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
	}

	private boolean canBePlacedOn(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).isSideSolid(worldIn, pos, EnumFacing.UP)|| worldIn.getBlockState(pos).getBlock() instanceof BlockFence;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}
}
