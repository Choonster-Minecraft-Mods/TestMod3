package choonster.testmod3.world.level.levelgen.surfacebuilders;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;
import net.minecraftforge.common.util.Lazy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.function.Supplier;

/**
 * A {@link SurfaceBuilder} that delegates to another {@link SurfaceBuilder} and
 * logs a message the first time it's used to generate a biome.
 *
 * @author Choonster
 */
public class LoggingSurfaceBuilder<C extends SurfaceBuilderConfiguration, S extends SurfaceBuilder<C>> extends SurfaceBuilder<C> {
	private static final Logger LOGGER = LogManager.getLogger();

	private final Lazy<S> delegatedSurfaceBuilder;

	private boolean logged = false;

	public LoggingSurfaceBuilder(final Supplier<S> delegatedSurfaceBuilder, final Codec<C> codec) {
		super(codec);
		this.delegatedSurfaceBuilder = Lazy.of(delegatedSurfaceBuilder);
	}

	@Override
	public void apply(
			final Random random, final ChunkAccess chunk, final Biome biome,
			final int x, final int z, final int startHeight, final double noise,
			final BlockState defaultBlock, final BlockState defaultFluid,
			final int seaLevel, final int minSurfaceLevel, final long seed,
			final C config
	) {
		delegatedSurfaceBuilder.get().apply(
				random, chunk, biome,
				x, z, startHeight, noise,
				defaultBlock, defaultFluid,
				seaLevel, minSurfaceLevel, seed,
				config
		);

		if (!logged) {
			logged = true;
			final ChunkPos chunkPos = chunk.getPos();
			LOGGER.info("Generating {} at {},{}", biome.getRegistryName(), chunkPos.getMinBlockX(), chunkPos.getMinBlockZ());
		}
	}
}