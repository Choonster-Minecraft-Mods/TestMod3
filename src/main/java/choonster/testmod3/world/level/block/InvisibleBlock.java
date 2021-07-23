package choonster.testmod3.world.level.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

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
	public boolean skipRendering(final BlockState state, final BlockState adjacentBlockState, final Direction side) {
		return true;
	}
}
