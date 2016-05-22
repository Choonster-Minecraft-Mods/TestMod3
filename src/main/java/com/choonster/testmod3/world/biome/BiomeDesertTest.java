package com.choonster.testmod3.world.biome;

import com.choonster.testmod3.Logger;
import net.minecraft.block.BlockSand;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

/**
 * A Desert-like biome with Red Sand as the top block and Brick Block as the filler block.
 *
 * @author Choonster
 */
public class BiomeDesertTest extends BiomeDesert {
	private boolean logged = false;

	public BiomeDesertTest(BiomeProperties properties) {
		super(properties);
		topBlock = Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
		fillerBlock = Blocks.BRICK_BLOCK.getDefaultState();
	}

	@Override
	public void genTerrainBlocks(World worldIn, Random random, ChunkPrimer chunkPrimer, int x, int z, double stoneNoise) {
		super.genTerrainBlocks(worldIn, random, chunkPrimer, x, z, stoneNoise);

		if (!logged) {
			logged = true;
			Logger.info("Generating desert test at %d,%d", x, z);
		}
	}
}
