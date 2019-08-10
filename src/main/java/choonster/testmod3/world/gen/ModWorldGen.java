package choonster.testmod3.world.gen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModFeatures;
import choonster.testmod3.init.ModPlacements;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModWorldGen {
	@SubscribeEvent
	public static void addFeaturesToBiomes(final FMLCommonSetupEvent event) {
		for (final Biome biome : ForgeRegistries.BIOMES) {
			/*
				Generates Banners with a specific pattern in chunks with coordinates divisible by 16.
				Only generates in dimensions that return true from WorldProvider#isSurfaceWorld.

				Test for this thread:
				http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
			*/
			biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(ModFeatures.BANNER, IFeatureConfig.NO_FEATURE_CONFIG, ModPlacements.AT_SURFACE_IN_SURFACE_WORLD_CHUNKS_DIVISIBLE_BY_16, new FrequencyConfig(1)));

			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, Blocks.IRON_ORE.getDefaultState(), 20), Placement.COUNT_RANGE, new CountRangeConfig(16, 10, 0, 118)));
			}

			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.END)) {
				// TODO: End Oregen
//				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(BlockMatcher.forBlock(Blocks.END_STONE), Blocks.IRON_ORE.getDefaultState(), 20), Placement.COUNT_RANGE, new CountRangeConfig(16, 0, 0, 128)));
			}
		}
	}
}
