package choonster.testmod3.world.gen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModConfiguredFeatures;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = TestMod3.MODID)
public class ModWorldGen {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void addFeaturesToBiomes(final BiomeLoadingEvent event) {
		final BiomeGenerationSettingsBuilder generation = event.getGeneration();

		final RegistryKey<Biome> biomeRegistryKey = RegistryKey.create(ForgeRegistries.Keys.BIOMES, Objects.requireNonNull(event.getName(), "Biome registry name was null"));

		/*
			Generates Banners with a specific pattern in chunks with coordinates divisible by 16.
			Only generates in dimensions that return true from WorldProvider#isSurfaceWorld.

			Test for this thread:
			http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
		*/
		if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.OVERWORLD)) {
			generation
					.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, getFeature(ModConfiguredFeatures.BANNER));
		}

		if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.NETHER)) {
			generation
					.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, getFeature(ModConfiguredFeatures.NETHER_IRON_ORE));
		}

		if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.END)) {
			generation
					.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, getFeature(ModConfiguredFeatures.END_IRON_ORE));
		}
	}

	private static ConfiguredFeature<?, ?> getFeature(final RegistryKey<ConfiguredFeature<?, ?>> key) {
		return WorldGenRegistries.CONFIGURED_FEATURE.getOrThrow(key);
	}
}
