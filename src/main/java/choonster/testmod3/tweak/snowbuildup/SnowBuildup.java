package choonster.testmod3.tweak.snowbuildup;

import choonster.testmod3.TestMod3;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

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
	private static final Method GET_CHUNKS = ObfuscationReflectionHelper.findMethod(ChunkMap.class, /* getChunks */ "func_223491_f");

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
	public static void onWorldTick(final TickEvent.WorldTickEvent event) {
		if (event.world.isClientSide) return;

		final ServerLevel world = (ServerLevel) event.world;

		// If this is the post tick, the world's total time (number of ticks) is divisible by NUM_TICKS, and it's raining/snowing,
		if (event.phase != TickEvent.Phase.END || world.getGameTime() % NUM_TICKS != 0 || !world.isRaining())
			return;

		final Iterable<ChunkHolder> loadedChunks;
		try {
			@SuppressWarnings("unchecked")
			final Iterable<ChunkHolder> chunks = (Iterable<ChunkHolder>) GET_CHUNKS.invoke(world.getChunkSource().chunkMap);
			loadedChunks = chunks;
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Couldn't get loaded chunks for Snow Buildup", e);
		}

		// For each loaded chunk
		loadedChunks.forEach(chunkHolder ->
				chunkHolder.getEntityTickingChunkFuture()
						.getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)
						.left()
						.ifPresent(chunk -> {
							for (int x = 0; x < 16; x++) {
								for (int z = 0; z < 16; z++) {
									// Get the position of top block at the current x and z coordinates within the chunk
									final BlockPos pos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, chunk.getPos().getWorldPosition().offset(x, 0, z));

									// Get the state of the block at that position
									final BlockState state = world.getBlockState(pos);

									// If the biome at that position allows snow, the block is a snow layer and a random integer in the range [0,24) is 0 (roughly 4% chance),
									if (world.getBiome(pos).shouldSnow(world, pos) && state.getBlock() == Blocks.SNOW && random.nextInt(24) == 0) {
										// Get the number of layers
										final int numLayers = state.getValue(SnowLayerBlock.LAYERS);

										if (numLayers < MAX_LAYERS) { // If it's less than the maximum, increase it by 1
											world.setBlockAndUpdate(pos, state.setValue(SnowLayerBlock.LAYERS, numLayers + 1));
										}
									}
								}
							}
						})
		);
	}
}
