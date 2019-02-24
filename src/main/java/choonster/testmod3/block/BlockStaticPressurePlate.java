package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;

/**
 * A block that uses pressure plate model, placement and piston movement behaviour; but doesn't depress when stood on.
 *
 * @author Choonster
 */
public abstract class BlockStaticPressurePlate extends Block {
	protected final VoxelShape SHAPE = makeCuboidShape(1, 0, 1, 15, 0.5, 15.0);

	public BlockStaticPressurePlate(final Block.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(final IBlockState state, final IBlockReader worldIn, final BlockPos pos) {
		return SHAPE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullCube(final IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState updatePostPlacement(final IBlockState state, final EnumFacing facing, final IBlockState facingState, final IWorld world, final BlockPos currentPos, final BlockPos facingPos) {
		return facing == EnumFacing.DOWN && !state.isValidPosition(world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValidPosition(final IBlockState state, final IWorldReaderBase world, final BlockPos pos) {
		final IBlockState downState = world.getBlockState(pos.down());
		return downState.isTopSolid() || downState.getBlock() instanceof BlockFence;
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(final IBlockState state, final IBlockReader world, final BlockPos pos) {
		return VoxelShapes.empty();
	}

	@SuppressWarnings("deprecation")
	@Override
	public EnumPushReaction getPushReaction(final IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockFaceShape getBlockFaceShape(final IBlockReader world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
}
