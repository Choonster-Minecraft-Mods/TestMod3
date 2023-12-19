package choonster.testmod3.world.level.block.pipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;

/**
 * A basic pipe that only connects to other pipes.
 *
 * @author Choonster
 */
public class BasicPipeBlock extends BasePipeBlock {
	public static final MapCodec<BasicPipeBlock> CODEC = simpleCodec(BasicPipeBlock::new);

	public BasicPipeBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends PipeBlock> codec() {
		return CODEC;
	}
}
