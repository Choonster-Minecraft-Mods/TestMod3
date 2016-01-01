package com.choonster.testmod3.tweak.snowbuildup;

import com.choonster.testmod3.Logger;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.Set;

/**
 * Allows snow layers to build up in areas where it's snowing.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2588089-get-active-chunks-and-get-list-of-all-snow-layer
 */
public class SnowBuildup {

	public static final SnowBuildup INSTANCE = new SnowBuildup();

	public static void init() {
		FMLCommonHandler.instance().bus().register(INSTANCE);
	}

	/**
	 * The number of ticks between each buildup.
	 */
	private final int NUM_TICKS = 10;

	/**
	 * The maximum number of layers per snow layer block
	 */
	private final int MAX_LAYERS = 8;

	/**
	 * A reference to the private World#activeChunkSet field
	 */
	private final Field ACTIVE_CHUNK_SET_FIELD = ReflectionHelper.findField(World.class, "activeChunkSet", "field_72993_I");

	/**
	 * The random number generator
	 */
	private final Random random = new Random();


	private SnowBuildup() {
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		final World world = event.world;

		// If this is the post tick, the world's total time (number of ticks) is divisible by NUM_TICKS and it's raining/snowing,
		if (event.phase == TickEvent.Phase.END && world.getTotalWorldTime() % NUM_TICKS == 0 && world.isRaining()) {
			try {
				// Get the coordinates of the world's loaded chunks
				@SuppressWarnings("unchecked")
				final Set<ChunkCoordIntPair> activeChunks = (Set<ChunkCoordIntPair>) ACTIVE_CHUNK_SET_FIELD.get(world);
				for (final ChunkCoordIntPair chunkCoordinates : activeChunks) {
					for (int x = 0; x < 16; x++) {
						for (int z = 0; z < 16; z++) {
							// Get the position of top block at the current x and z coordinates within the chunk
							final BlockPos pos = world.getHeight(new BlockPos(chunkCoordinates.chunkXPos * 16 + x, 0, chunkCoordinates.chunkZPos * 16 + z));

							// Get the state of the block at that position
							final IBlockState state = world.getBlockState(pos);

							// If the biome at that position allows snow, the block is a snow layer and a random integer in the range [0,24) is 0 (roughly 4% chance),
							if (world.getBiomeGenForCoords(pos).getEnableSnow() && state.getBlock() == Blocks.snow_layer && random.nextInt(24) == 0) {
								// Get the number of layers
								final int numLayers = (int) state.getValue(BlockSnow.LAYERS);

								if (numLayers < MAX_LAYERS) { // If it's less than the maximum, increase it by 1
									world.setBlockState(pos, state.withProperty(BlockSnow.LAYERS, numLayers + 1));
								}
							}
						}
					}
				}
			} catch (IllegalAccessException e) {
				Logger.error(e, "Unable to access World#activeChunkSet field");
			}
		}
	}
}
