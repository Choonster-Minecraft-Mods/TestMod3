package choonster.testmod3.world.gen.placement;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.Stream;

/**
 * Places a only in chunks with coordinates divisible by 16.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
 *
 * @author Choonster
 */
public class InChunksDivisibleBy16 extends Placement<NoPlacementConfig> {
	public InChunksDivisibleBy16(final Codec<NoPlacementConfig> codec) {
		super(codec);
	}

	@Override
	public Stream<BlockPos> getPositions(final WorldDecoratingHelper helper, final Random rand, final NoPlacementConfig config, final BlockPos pos) {
		final ChunkPos chunkPos = new ChunkPos(pos);

		if (chunkPos.x % 16 == 0 && chunkPos.z % 16 == 0) {
			return Stream.of(pos);
		}

		return Stream.empty();
	}
}
