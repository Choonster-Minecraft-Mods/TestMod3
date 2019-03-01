package choonster.testmod3.world.gen.placement;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.BasePlacement;
import net.minecraft.world.gen.placement.FrequencyConfig;

import java.util.Random;

/**
 * Places a feature at the surface, but only in chunks with coordinates divisible by 16 and only in dimensions that
 * return {@code true} from {@link Dimension#isSurfaceWorld()}.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
 *
 * @author Choonster
 */
public class AtSurfaceInSurfaceWorldChunksDivisibleBy16 extends BasePlacement<FrequencyConfig> {
	@Override
	public <C extends IFeatureConfig> boolean generate(final IWorld world, final IChunkGenerator<? extends IChunkGenSettings> chunkGenerator, final Random random, final BlockPos pos, final FrequencyConfig placementConfig, final Feature<C> feature, final C featureConfig) {
		final ChunkPos chunkPos = new ChunkPos(pos);

		if (!world.getDimension().isSurfaceWorld() || chunkPos.x % 16 != 0 || chunkPos.z % 16 != 0) {
			return false;
		}

		for (int i = 0; i < placementConfig.frequency; ++i) {
			final int x = random.nextInt(16);
			final int z = random.nextInt(16);
			feature.place(world, chunkGenerator, random, world.getHeight(Heightmap.Type.MOTION_BLOCKING, pos.add(x, 0, z)), featureConfig);
		}

		return false;
	}
}
