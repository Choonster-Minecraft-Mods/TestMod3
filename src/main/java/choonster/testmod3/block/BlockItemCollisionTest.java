package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A block that writes a message to the log when an item collides with it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34022.0.html
 *
 * @author Choonster
 */
public class BlockItemCollisionTest extends Block {
	private static final Logger LOGGER = LogManager.getLogger();

	private static final VoxelShape SHAPE;

	static {
		// A small value to offset each side of the block's bounding box by to allow entities to collide with the block
		// and thus call onEntityCollidedWithBlock
		final float minBound = 0.16f;
		final float maxBound = 16 - minBound;

		SHAPE = makeCuboidShape(minBound, minBound, minBound, maxBound, maxBound, maxBound);
	}

	public BlockItemCollisionTest(final Block.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEntityCollision(final IBlockState state, final World world, final BlockPos pos, final Entity entityIn) {
		super.onEntityCollision(state, world, pos, entityIn);

		if (!world.isRemote && entityIn instanceof EntityItem) {
			LOGGER.info("Collision at {}: {}", pos, entityIn);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(final IBlockState state, final IBlockReader worldIn, final BlockPos pos) {
		return SHAPE;
	}
}
