package com.choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * A block that uses pressure plate model, placement and piston movement behaviour; but doesn't depress when stood on.
 *
 * @author Choonster
 */
public abstract class BlockStaticPressurePlate extends BlockTestMod3 {
	public BlockStaticPressurePlate(Material materialIn, String blockName) {
		super(materialIn, blockName);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
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
		return World.doesBlockHaveSolidTopSurface(worldIn, pos) || worldIn.getBlockState(pos).getBlock() instanceof BlockFence;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.0625F, 0.9375F);
	}

	@Override
	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.375F, 0.0F, 1.0F, 0.625F, 1.0F);
	}

	@Override
	public int getMobilityFlag() {
		return 1;
	}
}
