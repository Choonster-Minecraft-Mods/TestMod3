package com.choonster.testmod3.world.biome;

import com.choonster.testmod3.Logger;
import net.minecraft.block.BlockSand;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenDesert;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGenDesertTest extends BiomeGenDesert {
	private boolean logged = false;

	public BiomeGenDesertTest(int id) {
		super(id);
		topBlock = Blocks.sand.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
		fillerBlock = Blocks.brick_block.getDefaultState();
		setBiomeName("testmod3:Desert Test");
		setColor(16421912);
		setTemperatureRainfall(2.0F, 0.0F);
		setHeight(height_LowPlains);
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
