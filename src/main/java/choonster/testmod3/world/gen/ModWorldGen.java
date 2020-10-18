package choonster.testmod3.world.gen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModFeatures;
import choonster.testmod3.init.ModPlacements;
import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TestMod3.MODID)
public class ModWorldGen {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void addFeaturesToBiomes(final BiomeLoadingEvent event) {
		final BiomeGenerationSettingsBuilder generation = event.getGeneration();

		final RegistryKey<Biome> biomeRegistryKey = RegistryKey.getOrCreateKey(ForgeRegistries.Keys.BIOMES, event.getName());

		/*
				Generates Banners with a specific pattern in chunks with coordinates divisible by 16.
				Only generates in dimensions that return true from WorldProvider#isSurfaceWorld.

				Test for this thread:
				http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
			*/

		if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.OVERWORLD)) {
			generation.getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES)
					.add(() ->
							ModFeatures.BANNER.get()
									.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
									.withPlacement(ModPlacements.AT_SURFACE_IN_CHUNKS_DIVISIBLE_BY_16.get().configure(new FeatureSpreadConfig(1)))
					);
		}

		if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.NETHER)) {
			generation.getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES)
					.add(() -> Feature.ORE
							.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241883_b, Blocks.IRON_ORE.getDefaultState(), 20))
							.func_242733_d(118) // maximum Y?
							.func_242728_a() // square
							.func_242731_b(16) // count?
					);
		}

		if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.END)) {
			generation.getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES)
					.add(() -> Feature.ORE
							.withConfiguration(new OreFeatureConfig(FillerBlockType.END_STONE, Blocks.IRON_ORE.getDefaultState(), 20))
							.func_242733_d(128) // maximum Y?
							.func_242728_a() // square
							.func_242731_b(16) // count?
					);
		}
	}

	public static class FillerBlockType {
		public static final RuleTest END_STONE = new BlockMatchRuleTest(Blocks.END_STONE);
	}
}
