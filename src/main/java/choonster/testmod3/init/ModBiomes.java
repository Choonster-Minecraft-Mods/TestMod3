package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.biome.DesertTestBiome;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static choonster.testmod3.util.InjectionUtil.Null;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModBiomes {

	public static final DesertTestBiome DESERT_TEST = Null();


	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class RegistrationHandler {
		/**
		 * Register this mod's {@link Biome}s.
		 *
		 * @param event The event
		 */
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
