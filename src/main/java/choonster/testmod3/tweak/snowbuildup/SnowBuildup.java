package choonster.testmod3.tweak.snowbuildup;

import choonster.testmod3.TestMod3;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Allows snow layers to build up in areas where it's snowing.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2588089-get-active-chunks-and-get-list-of-all-snow-layer
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID)
public class SnowBuildup {
	private static final Method FOR_EACH_BLOCK_TICKING_CHUNK = ObfuscationReflectionHelper.findMethod(
			ChunkMap.class,
			"forEachBlockTickingChunk",
			Consumer.class
	);

	/**
	 * The number of ticks between each buildup.
	 */
	private static final int NUM_TICKS = 10;

	/**
	 * The maximum number of layers per snow layer block
	 */
	private static final int MAX_LAYERS = 8;

	/**
	 * The random number generator
	 */
	private static final Random random = new Random();

	@SubscribeEvent
	public static void onLevelTick(final TickEvent.LevelTickEvent.Post event) {
		if (event.level().isClientSide() || !(event.level() instanceof final ServerLevel level)) {
			return;
		}

		// If the level's total time (number of ticks) is divisible by NUM_TICKS, and it's raining/snowing,
		if (level.getGameTime() % NUM_TICKS != 0 || !level.isRaining()) {
			return;
		}

		final Consumer<LevelChunk> handleChunk = SnowBuildup::handleChunk;

		final Iterable<ChunkHolder> loadedChunks;
		try {
			FOR_EACH_BLOCK_TICKING_CHUNK.invoke(level.getChunkSource().chunkMap, handleChunk);
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Couldn't process loaded chunks for Snow Buildup", e);
		}
	}

	private static void handleChunk(final LevelChunk chunk) {
		final var level = chunk.getLevel();
		final var chunkPos = chunk.getPos();

		final var positions = BlockPos.betweenClosed(
				new BlockPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ()),
				new BlockPos(chunkPos.getMaxBlockX(), 0, chunkPos.getMinBlockZ())
		);

		for (final var minPos : positions) {
			// Get the position of top block at the current x and z coordinates within the chunk
			final var pos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, minPos);

			// Get the state of the block at that position
			final var state = level.getBlockState(pos);

			// If the biome at that position allows snow, the block is a snow layer and a random integer in the range [0,24) is 0 (roughly 4% chance),
			if (level.getBiome(pos).value().shouldSnow(level, pos) && state.getBlock() == Blocks.SNOW && random.nextInt(24) == 0) {
				// Get the number of layers
				final int numLayers = state.getValue(SnowLayerBlock.LAYERS);

				if (numLayers < MAX_LAYERS) { // If it's less than the maximum, increase it by 1
					level.setBlockAndUpdate(pos, state.setValue(SnowLayerBlock.LAYERS, numLayers + 1));
				}
			}
		}
	}
}
