package choonster.testmod3.world.level.levelgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModConfiguredFeatures;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = TestMod3.MODID)
public class ModLevelGen {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void addFeaturesToBiomes(final BiomeLoadingEvent event) {
		final BiomeGenerationSettingsBuilder generation = event.getGeneration();

		final ResourceKey<Biome> biomeRegistryKey = ResourceKey.create(ForgeRegistries.Keys.BIOMES, Objects.requireNonNull(event.getName(), "Biome registry name was null"));

		/*
			Generates Banners with a specific pattern in chunks with coordinates divisible by 16.
			Only generates in biomes with the Overworld type in the Biome Dictionary.

			Test for this thread:
			http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
		*/
		if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.OVERWORLD)) {
			generation
					.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, getFeature(ModConfiguredFeatures.BANNER));
		}

		if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.NETHER)) {
			generation
					.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, getFeature(ModConfiguredFeatures.NETHER_IRON_ORE));
		}

		if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.END)) {
			generation
					.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, getFeature(ModConfiguredFeatures.END_IRON_ORE));
		}
	}

	private static ConfiguredFeature<?, ?> getFeature(final ResourceKey<ConfiguredFeature<?, ?>> key) {
		return BuiltinRegistries.CONFIGURED_FEATURE.getOrThrow(key);
	}
}
