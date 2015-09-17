package com.choonster.testmod3.block.pipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BlockPipeBase extends Block {
	public static final float PIPE_MIN_POS = 0.25f;
	public static final float PIPE_MAX_POS = 0.75f;

	public static final ImmutableList<IProperty> CONNECTED_PROPERTIES = ImmutableList.copyOf(
			Stream.of(EnumFacing.VALUES)
					.map(facing -> PropertyBool.create(facing.getName()))
					.collect(Collectors.toList())
	);

	public static final ImmutableList<AxisAlignedBB> CONNECTED_BOUNDING_BOXES = ImmutableList.copyOf(
			Stream.of(EnumFacing.VALUES)
					.map(facing -> {
						Vec3i directionVec = facing.getDirectionVec();
						return new AxisAlignedBB(
								getMinBound(directionVec.getX()), getMinBound(directionVec.getY()), getMinBound(directionVec.getZ()),
								getMaxBound(directionVec.getX()), getMaxBound(directionVec.getY()), getMaxBound(directionVec.getZ())
						);
					})
					.collect(Collectors.toList())
	);

	private static float getMinBound(int dir) {
		return dir == -1 ? 0 : PIPE_MIN_POS;
	}

	private static float getMaxBound(int dir) {
		return dir == 1 ? 1 : PIPE_MAX_POS;
	}

	public BlockPipeBase(Material material) {
		super(material);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * Is the neighbouring pipe a valid connection for this pipe?
	 *
	 * @param ownState           This pipe's state
	 * @param neighbourState     The neighbouring pipe's state
	 * @param world              The world
	 * @param ownPos             This pipe's position
	 * @param neighbourDirection The direction of the neighbouring pipe
	 * @return Is the neighbouring pipe a valid connection?
	 */
	protected boolean isValidPipe(IBlockState ownState, IBlockState neighbourState, IBlockAccess world, BlockPos ownPos, EnumFacing neighbourDirection) {
		return neighbourState.getBlock() instanceof BlockPipeBase;
	}

	/**
	 * Can this pipe connect to the neighbouring block?
	 *
	 * @param ownState           This pipe's state
	 * @param worldIn            The world
	 * @param ownPos             This pipe's position
	 * @param neighbourDirection The direction of the neighbouring block
	 * @return Can this pipe connect?
	 */
	private boolean canConnectTo(IBlockState ownState, IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection) {
		BlockPos neighbourPos = ownPos.offset(neighbourDirection);
		IBlockState neighbourState = worldIn.getBlockState(neighbourPos);
		Block neighbourBlock = neighbourState.getBlock();

		boolean neighbourIsValidForThis = isValidPipe(ownState, neighbourState, worldIn, ownPos, neighbourDirection);
		boolean thisIsValidForNeighbour = neighbourBlock instanceof BlockPipeBase && ((BlockPipeBase) neighbourBlock).isValidPipe(neighbourState, ownState, worldIn, neighbourPos, neighbourDirection.getOpposite());

		return neighbourIsValidForThis && thisIsValidForNeighbour;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		for (EnumFacing facing : EnumFacing.VALUES) {
			state = state.withProperty(CONNECTED_PROPERTIES.get(facing.getIndex()), canConnectTo(state, world, pos, facing));
		}

		return state;
	}

	public final boolean isConnected(IBlockState state, EnumFacing facing) {
		return (boolean) state.getValue(CONNECTED_PROPERTIES.get(facing.getIndex()));
	}

	public void setBlockBounds(AxisAlignedBB bb) {
		setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
	}

	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
		setBlockBounds(PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MAX_POS, PIPE_MAX_POS, PIPE_MAX_POS);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);

		state = getActualState(state, worldIn, pos);

		for (EnumFacing facing : EnumFacing.VALUES) {
			if (isConnected(state, facing)) {
				AxisAlignedBB axisAlignedBB = CONNECTED_BOUNDING_BOXES.get(facing.getIndex());
				setBlockBounds(axisAlignedBB);
				super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
			}
		}

		setBlockBounds(0, 0, 0, 1, 1, 1);
	}
}
