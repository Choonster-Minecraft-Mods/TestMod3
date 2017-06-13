package choonster.testmod3.tweak.snowbuildup;

import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;
import java.util.Random;

/**
 * Allows snow layers to build up in areas where it's snowing.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2588089-get-active-chunks-and-get-list-of-all-snow-layer
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber
public class SnowBuildup {

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
		if (event.world.isRemote) return;

		final WorldServer world = (WorldServer) event.world;

		// If this is the post tick, the world's total time (number of ticks) is divisible by NUM_TICKS and it's raining/snowing,
		if (event.phase != TickEvent.Phase.END || world.getTotalWorldTime() % NUM_TICKS != 0 || !world.isRaining())
			return;

		// For each loaded chunk
		for (final Iterator<Chunk> iterator = world.getPersistentChunkIterable(world.getPlayerChunkMap().getChunkIterator()); iterator.hasNext(); ) {
			final Chunk chunk = iterator.next();

			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					// Get the position of top block at the current x and z coordinates within the chunk
					final BlockPos pos = world.getHeight(chunk.getPos().getBlock(x, 0, z));

					// Get the state of the block at that position
					final IBlockState state = world.getBlockState(pos);

					// If the biome at that position allows snow, the block is a snow layer and a random integer in the range [0,24) is 0 (roughly 4% chance),
					if (world.getBiome(pos).getEnableSnow() && state.getBlock() == Blocks.SNOW_LAYER && random.nextInt(24) == 0) {
						// Get the number of layers
						final int numLayers = state.getValue(BlockSnow.LAYERS);

						if (numLayers < MAX_LAYERS) { // If it's less than the maximum, increase it by 1
							world.setBlockState(pos, state.withProperty(BlockSnow.LAYERS, numLayers + 1));
						}
					}
				}
			}
		}
	}
}
