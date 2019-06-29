package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

/**
 * A block that doesn't render in the world.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php?/topic/44951-obj-models/
 *
 * @author Choonster
 */
public class InvisibleBlock extends Block {
	public InvisibleBlock(final Block.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isSideInvisible(final BlockState state, final BlockState adjacentBlockState, final Direction side) {
		return true;
	}
}
