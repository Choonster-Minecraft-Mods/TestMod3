package choonster.testmod3.world.gen.surfacebuilders;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/**
 * A {@link ConfiguredSurfaceBuilder} that logs a message the first time it's used to generate a biome.
 *
 * @author Choonster
 */
public class LoggingConfiguredSurfaceBuilder<C extends ISurfaceBuilderConfig> extends ConfiguredSurfaceBuilder<C> {
	private static final Logger LOGGER = LogManager.getLogger();

	private boolean logged = false;

	public LoggingConfiguredSurfaceBuilder(final SurfaceBuilder<C> surfaceBuilder, final C config) {
		super(surfaceBuilder, config);
	}

	@Override
	public void buildSurface(
			final Random random, final IChunk chunk, final Biome biome,
			final int x, final int z, final int startHeight, final double noise,
			final BlockState defaultBlock, final BlockState defaultFluid,
			final int seaLevel, final long seed
	) {
		super.buildSurface(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed);

		if (!logged) {
			logged = true;
			final ChunkPos chunkPos = chunk.getPos();
			LOGGER.info("Generating {} at {},{}", biome.getRegistryName(), chunkPos.getXStart(), chunkPos.getZStart());
		}
	}
}