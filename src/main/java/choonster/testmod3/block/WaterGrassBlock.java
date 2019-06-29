package choonster.testmod3.block;

import choonster.testmod3.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

/**
 * Tall grass that renders with water around it while in water.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/32069-18-solved-water-and-coral-in-one-block/
 *
 * @author Choonster
 */
public class WaterGrassBlock extends BushBlock {
	private static final VoxelShape SHAPE = Util.make(() -> {
		final float size = 6.4f;
		return makeCuboidShape(8 - size, 0, 8 - size, 8 + size, 12.8, 8 + size);
	});

	public WaterGrassBlock(final Properties properties) {
		super(properties);
		// TODO: Figure out fluid level/waterlogged state
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(final BlockState state, final IBlockReader world, final BlockPos pos, final ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public BlockState updatePostPlacement(final BlockState state, final Direction facing, final BlockState facingState, final IWorld world, final BlockPos currentPos, final BlockPos facingPos) {
		return state.isValidPosition(world, currentPos) ? Blocks.WATER.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
	}

	@Override
	public boolean isValidPosition(final BlockState state, final IWorldReader world, final BlockPos pos) {
		return world.getBlockState(pos.up()).getBlock() == Blocks.WATER && super.isValidPosition(state, world, pos);
	}

	@Override
	public void onPlayerDestroy(final IWorld world, final BlockPos pos, final BlockState state) {
		world.setBlockState(pos, Blocks.WATER.getDefaultState(), Constants.BlockFlags.DEFAULT_FLAGS);
	}
}
