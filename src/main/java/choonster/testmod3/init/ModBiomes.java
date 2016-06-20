package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.biome.BiomeDesertTest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

@SuppressWarnings("WeakerAccess")
public class ModBiomes {
	public static final BiomeDesertTest DESERT_TEST;

	static {
		DESERT_TEST = registerBiome(new BiomeDesertTest(new Biome.BiomeProperties("TestMod3 Desert Test")
				.setBaseHeight(0.125F)
				.setHeightVariation(0.05F)
				.setTemperature(2.0F)
				.setRainfall(0.0F)
				.setRainDisabled()
		), new ResourceLocation(TestMod3.MODID, "desert_test"), BiomeManager.BiomeType.DESERT, 1000, HOT, DRY, SANDY, JUNGLE, SWAMP);
	}

	public static void registerBiomes() {
		// Dummy method to make sure the static initialiser runs
	}

	private static <T extends Biome> T registerBiome(T biome, ResourceLocation biomeName, BiomeManager.BiomeType biomeType, int weight, BiomeDictionary.Type... types) {
		GameRegistry.register(biome.setRegistryName(biomeName));
		BiomeDictionary.registerBiomeType(biome, types);
		BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(biome, weight));

		return biome;
	}
}
