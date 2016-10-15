package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.biome.BiomeDesertTest;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModBiomes {

	@ObjectHolder("desert_test")
	public static final BiomeDesertTest DESERT_TEST = new BiomeDesertTest(new Biome.BiomeProperties("TestMod3 Desert Test")
			.setBaseHeight(0.125F)
			.setHeightVariation(0.05F)
			.setTemperature(2.0F)
			.setRainfall(0.0F)
			.setRainDisabled()
	);

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {
		/**
		 * Register this mod's {@link Biome}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerBiomes(RegistryEvent.Register<Biome> event) {
			final IForgeRegistry<Biome> registry = event.getRegistry();

			registerBiome(registry, DESERT_TEST, "desert_test", BiomeManager.BiomeType.DESERT, 1000, HOT, DRY, SANDY, JUNGLE, SWAMP);
		}

		private static <T extends Biome> T registerBiome(IForgeRegistry<Biome> registry, T biome, String biomeName, BiomeManager.BiomeType biomeType, int weight, BiomeDictionary.Type... types) {
			registry.register(biome.setRegistryName(TestMod3.MODID, biomeName));
			BiomeDictionary.registerBiomeType(biome, types);
			BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(biome, weight));

			return biome;
		}

	}
}
