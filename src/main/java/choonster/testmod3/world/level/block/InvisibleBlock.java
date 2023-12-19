package choonster.testmod3.world.level.block;

import com.mojang.serialization.MapCodec;
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
	public static final MapCodec<InvisibleBlock> CODEC = simpleCodec(InvisibleBlock::new);

	public InvisibleBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean skipRendering(final BlockState state, final BlockState adjacentBlockState, final Direction side) {
		return true;
	}
}
