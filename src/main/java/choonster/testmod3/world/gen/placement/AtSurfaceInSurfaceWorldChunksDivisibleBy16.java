package choonster.testmod3.world.gen.placement;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Places a feature at the surface, but only in chunks with coordinates divisible by 16 and only in dimensions that
 * return {@code true} from {@link Dimension#isSurfaceWorld()}.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
 *
 * @author Choonster
 */
public class AtSurfaceInSurfaceWorldChunksDivisibleBy16 extends Placement<FrequencyConfig> {
	public AtSurfaceInSurfaceWorldChunksDivisibleBy16(final Function<Dynamic<?>, ? extends FrequencyConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public Stream<BlockPos> getPositions(final IWorld world, final ChunkGenerator<? extends GenerationSettings> chunkGenerator, final Random random, final FrequencyConfig placementConfig, final BlockPos pos) {

		final ChunkPos chunkPos = new ChunkPos(pos);

		if (!world.getDimension().isSurfaceWorld() || chunkPos.x % 16 != 0 || chunkPos.z % 16 != 0) {
			return Stream.empty();
		}

		return IntStream.range(0, placementConfig.count).mapToObj(i -> {
			final int x = random.nextInt(16);
			final int z = random.nextInt(16);
			return world.getHeight(Heightmap.Type.MOTION_BLOCKING, pos.add(x, 0, z));
		});
	}
}
