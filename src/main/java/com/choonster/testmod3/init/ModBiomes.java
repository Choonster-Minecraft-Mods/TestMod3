package com.choonster.testmod3.init;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.world.biome.BiomeGenDesertTest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

@SuppressWarnings("WeakerAccess")
public class ModBiomes {
	public static BiomeGenDesertTest desertTest;

	public static void registerBiomes() {
		desertTest = registerBiome(new BiomeGenDesertTest(new BiomeGenBase.BiomeProperties("TestMod3 Desert Test")
				.setBaseHeight(0.125F)
				.setHeightVariation(0.05F)
				.setTemperature(2.0F)
				.setRainfall(0.0F)
				.setRainDisabled()
		), new ResourceLocation(TestMod3.MODID, "desert_test"), BiomeManager.BiomeType.DESERT, 1000, HOT, DRY, SANDY, JUNGLE, SWAMP);
	}

	private static <T extends BiomeGenBase> T registerBiome(T biome, ResourceLocation biomeName, BiomeManager.BiomeType biomeType, int weight, BiomeDictionary.Type... types) {
		GameRegistry.register(biome, biomeName);
		BiomeDictionary.registerBiomeType(biome, types);
		BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(biome, weight));

		return biome;
	}
}
