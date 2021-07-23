package choonster.testmod3.world.gen.surfacebuilders;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
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
public class LoggingSurfaceBuilder<C extends ISurfaceBuilderConfig, S extends SurfaceBuilder<C>> extends SurfaceBuilder<C> {
	private static final Logger LOGGER = LogManager.getLogger();

	private final Lazy<S> delegatedSurfaceBuilder;

	private boolean logged = false;

	public LoggingSurfaceBuilder(final Supplier<S> delegatedSurfaceBuilder, final Codec<C> codec) {
		super(codec);
		this.delegatedSurfaceBuilder = Lazy.of(delegatedSurfaceBuilder);
	}

	@Override
	public void apply(
			final Random random, final IChunk chunk, final Biome biome,
			final int x, final int z, final int startHeight, final double noise,
			final BlockState defaultBlock, final BlockState defaultFluid,
			final int seaLevel, final long seed,
			final C config
	) {
		delegatedSurfaceBuilder.get().apply(
				random, chunk, biome,
				x, z, startHeight, noise,
				defaultBlock, defaultFluid,
				seaLevel, seed, config
		);

		if (!logged) {
			logged = true;
			final ChunkPos chunkPos = chunk.getPos();
			LOGGER.info("Generating {} at {},{}", biome.getRegistryName(), chunkPos.getMinBlockX(), chunkPos.getMinBlockZ());
		}
	}
}