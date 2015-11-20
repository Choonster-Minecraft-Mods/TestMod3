package com.choonster.testmod3.block;

import com.choonster.testmod3.tileentity.TileEntityColoredRotatable;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * A block with 16 colours (stored in the metadata) and 6 facings (stored in the TileEntity).
 */
public class BlockColoredRotatable extends BlockColored {
	public static final IProperty FACING = PropertyDirection.create("facing");

	public BlockColoredRotatable(Material materialIn) {
		super(materialIn);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, BlockColored.COLOR, FACING);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityColoredRotatable();
	}

	public TileEntityColoredRotatable getTileEntity(IBlockAccess world, BlockPos pos) {
		return (TileEntityColoredRotatable) world.getTileEntity(pos);
	}

	public EnumFacing getFacing(IBlockAccess world, BlockPos pos) {
		return getTileEntity(world, pos).getFacing();
	}

	public void setFacing(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		getTileEntity(world, pos).setFacing(facing);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		setFacing(worldIn, pos, BlockPistonBase.getFacingFromEntity(worldIn, pos, placer));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(FACING, getFacing(worldIn, pos));
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		EnumFacing facing = getFacing(world, pos);
		setFacing(world, pos, facing.rotateAround(axis.getAxis()));
		world.markBlockForUpdate(pos);

		return true;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking()) {
			return rotateBlock(worldIn, pos, side);
		} else {
			return worldIn.setBlockState(pos, state.cycleProperty(COLOR));
		}
	}
}
