package com.choonster.testmod3.world.gen;

import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenOres implements IWorldGenerator {
	private final WorldGenMinable oreGenNether;
	private final WorldGenMinable oreGenEnd;

	public WorldGenOres() {
		oreGenNether = new WorldGenMinable(Blocks.iron_ore.getDefaultState(), 20, BlockHelper.forBlock(Blocks.netherrack));
		oreGenEnd = new WorldGenMinable(Blocks.iron_ore.getDefaultState(), 20, BlockHelper.forBlock(Blocks.end_stone));
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		BlockPos chunkPos = new BlockPos(chunkX * 16, 0, chunkZ * 16);

		switch (world.provider.getDimensionId()) {
			case -1:
				for (int i = 0; i < 16; i++) {
					oreGenNether.generate(world, random, chunkPos.add(random.nextInt(16), random.nextInt(108) + 10, random.nextInt(16)));
				}
				break;
			case 1:
				for (int i = 0; i < 16; i++) {
					oreGenEnd.generate(world, random, chunkPos.add(random.nextInt(16), random.nextInt(128), random.nextInt(16)));
				}
				break;
		}
	}
}
