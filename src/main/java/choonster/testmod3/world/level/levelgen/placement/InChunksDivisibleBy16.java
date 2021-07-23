package choonster.testmod3.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

import java.util.Random;
import java.util.stream.Stream;

/**
 * Only generates positions in chunks with coordinates divisible by 16.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
 *
 * @author Choonster
 */
public class InChunksDivisibleBy16 extends FeatureDecorator<NoneDecoratorConfiguration> {
	public InChunksDivisibleBy16(final Codec<NoneDecoratorConfiguration> codec) {
		super(codec);
	}

	@Override
	public Stream<BlockPos> getPositions(final DecorationContext helper, final Random rand, final NoneDecoratorConfiguration config, final BlockPos pos) {
		final ChunkPos chunkPos = new ChunkPos(pos);

		if (chunkPos.x % 16 == 0 && chunkPos.z % 16 == 0) {
			return Stream.of(pos);
		}

		return Stream.empty();
	}
}
