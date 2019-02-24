package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

/**
 * A block that doesn't render in the world.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php?/topic/44951-obj-models/
 *
 * @author Choonster
 */
public class BlockInvisible extends Block {
	public BlockInvisible(final Block.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isSideInvisible(final IBlockState state, final IBlockState adjacentBlockState, final EnumFacing side) {
		return true;
	}
}
