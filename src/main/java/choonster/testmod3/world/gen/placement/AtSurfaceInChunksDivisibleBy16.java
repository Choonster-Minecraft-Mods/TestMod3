package choonster.testmod3.world.gen.placement;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Places a feature at the surface, but only in chunks with coordinates divisible by 16.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
 *
 * @author Choonster
 */
public class AtSurfaceInChunksDivisibleBy16 extends Placement<FeatureSpreadConfig> {
	public AtSurfaceInChunksDivisibleBy16(final Codec<FeatureSpreadConfig> codec) {
		super(codec);
	}

	@Override
	public Stream<BlockPos> func_241857_a(final WorldDecoratingHelper worldDecoratingHelper, final Random random, final FeatureSpreadConfig config, final BlockPos pos) {

		final ChunkPos chunkPos = new ChunkPos(pos);

		if (chunkPos.x % 16 != 0 || chunkPos.z % 16 != 0) {
			return Stream.empty();
		}

		return IntStream.range(0, config.func_242799_a().func_242259_a(random)).mapToObj(i -> {
			final int x = random.nextInt(16);
			final int z = random.nextInt(16);

			final BlockPos.Mutable mutablePos = pos.toMutable();
			mutablePos.add(x, 0, z);

			mutablePos.setY(worldDecoratingHelper.func_242893_a(Heightmap.Type.MOTION_BLOCKING, mutablePos.getX(), mutablePos.getZ()));

			return mutablePos;
		});
	}
}
