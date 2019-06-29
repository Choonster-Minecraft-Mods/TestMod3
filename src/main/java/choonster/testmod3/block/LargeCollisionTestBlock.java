package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

/**
 * A Block with a 3x3x3 bounding box.
 * <p>
 * Currently only the selection bounding box works.
 * Entity collision still treats the bounding box as 1x1x1 and glitches out if you try to enter this bounding box.
 *
 * @author Choonster
 */
public class LargeCollisionTestBlock extends Block {
	private static final VoxelShape SHAPE = makeCuboidShape(-16, -16, -16, 32, 32, 32);

	public LargeCollisionTestBlock(final Block.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(final BlockState state, final IBlockReader world, final BlockPos pos, final ISelectionContext context) {
		return SHAPE;
	}
}
