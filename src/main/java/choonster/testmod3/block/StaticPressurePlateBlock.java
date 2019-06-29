package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

/**
 * A block that uses pressure plate model, placement and piston movement behaviour; but doesn't depress when stood on.
 *
 * @author Choonster
 */
public abstract class StaticPressurePlateBlock extends Block {
	protected final VoxelShape SHAPE = makeCuboidShape(1, 0, 1, 15, 0.5, 15.0);

	public StaticPressurePlateBlock(final Block.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(final BlockState state, final IBlockReader world, final BlockPos pos, final ISelectionContext context) {
		return SHAPE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(final BlockState state, final Direction facing, final BlockState facingState, final IWorld world, final BlockPos currentPos, final BlockPos facingPos) {
		return facing == Direction.DOWN && !state.isValidPosition(world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValidPosition(final BlockState state, final IWorldReader world, final BlockPos pos) {
		final BlockPos downPos = pos.down();
		return func_220064_c(world, downPos) || func_220055_a(world, downPos, Direction.UP);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(final BlockState state, final IBlockReader world, final BlockPos pos, final ISelectionContext context) {
		return VoxelShapes.empty();
	}

	@SuppressWarnings("deprecation")
	@Override
	public PushReaction getPushReaction(final BlockState state) {
		return PushReaction.DESTROY;
	}
}
