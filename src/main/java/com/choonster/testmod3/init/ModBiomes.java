package com.choonster.testmod3.init;

import com.choonster.testmod3.config.Config;
import com.choonster.testmod3.world.biome.BiomeGenDesertTest;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

public class ModBiomes {
	public static BiomeGenDesertTest desertTest;

	public static void registerBiomes() {
		desertTest = reigsterBiome(new BiomeGenDesertTest(Config.desertBiomeID), BiomeManager.BiomeType.DESERT, 1000, HOT, DRY, SANDY);
	}

	private static <T extends BiomeGenBase> T reigsterBiome(T biome, BiomeManager.BiomeType biomeType, int weight, BiomeDictionary.Type... types) {
		BiomeDictionary.registerBiomeType(biome, types);
		BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(biome, weight));

		return biome;
	}
}
