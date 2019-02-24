package choonster.testmod3.block;

import choonster.testmod3.util.Constants;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;

/**
 * Tall grass that renders with water around it while in water.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/32069-18-solved-water-and-coral-in-one-block/
 *
 * @author Choonster
 */
public class BlockWaterGrass extends BlockBush {
	private static final VoxelShape SHAPE = Util.make(() -> {
		final float size = 6.4f;
		return makeCuboidShape(8 - size, 0, 8 - size, 8 + size, 12.8, 8 + size);
	});

	public BlockWaterGrass(final Properties properties) {
		super(properties);
		// TODO: Figure out fluid level/waterlogged state
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(final IBlockState state, final IBlockReader world, final BlockPos pos) {
		return SHAPE;
	}

	@Override
	public IBlockState updatePostPlacement(final IBlockState state, final EnumFacing facing, final IBlockState facingState, final IWorld world, final BlockPos currentPos, final BlockPos facingPos) {
		return state.isValidPosition(world, currentPos) ? Blocks.WATER.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
	}

	@Override
	public boolean isValidPosition(final IBlockState state, final IWorldReaderBase world, final BlockPos pos) {
		return world.getBlockState(pos.up()).getBlock() == Blocks.WATER && super.isValidPosition(state, world, pos);
	}

	@Override
	public void onPlayerDestroy(final IWorld world, final BlockPos pos, final IBlockState state) {
		world.setBlockState(pos, Blocks.WATER.getDefaultState(), Constants.BlockFlags.DEFAULT_FLAGS);
	}
}
