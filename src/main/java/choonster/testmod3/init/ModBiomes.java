package choonster.testmod3.init;

import net.minecraft.world.biome.Biome;
/*
// TODO: Replace with JSON biomes. From data generator?
@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModBiomes {

	public static final DesertTestBiome DESERT_TEST = Null();


	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		*/
/**
 * Register this mod's {@link Biome}s.
 *
 * @param event The event
 *//*

		@SubscribeEvent
		public static void registerBiomes(final RegistryEvent.Register<Biome> event) {
			final IForgeRegistry<Biome> registry = event.getRegistry();

			registerBiome(registry, new DesertTestBiome(), "desert_test", BiomeManager.BiomeType.DESERT, 1000, HOT, DRY, SANDY, JUNGLE, SWAMP);
		}

		private static <T extends Biome> void registerBiome(final IForgeRegistry<Biome> registry, final T biome, final String biomeName, final BiomeManager.BiomeType biomeType, final int weight, final BiomeDictionary.Type... types) {
			registry.register(biome.setRegistryName(TestMod3.MODID, biomeName));
			BiomeDictionary.addTypes(biome, types);
			BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(biome, weight));
		}
	}
}
*/
