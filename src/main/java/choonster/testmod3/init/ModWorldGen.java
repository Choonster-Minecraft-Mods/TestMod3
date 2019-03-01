package choonster.testmod3.init;

import choonster.testmod3.world.gen.feature.BannerFeature;
import choonster.testmod3.world.gen.placement.AtSurfaceInSurfaceWorldChunksDivisibleBy16;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.BasePlacement;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class ModWorldGen {
	public static class Features {
		public static final Feature<NoFeatureConfig> BANNER = new BannerFeature();
	}

	public static class Placements {
		public static final BasePlacement<FrequencyConfig> AT_SURFACE_IN_SURFACE_WORLD_CHUNKS_DIVISIBLE_BY_16 = new AtSurfaceInSurfaceWorldChunksDivisibleBy16();
	}

	public static void registerFeatures() {
		for (final Biome biome : ForgeRegistries.BIOMES) {
			/*
				Generates Banners with a specific pattern in chunks with coordinates divisible by 16.
				Only generates in dimensions that return true from WorldProvider#isSurfaceWorld.

				Test for this thread:
				http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
			*/
			biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createCompositeFeature(Features.BANNER, IFeatureConfig.NO_FEATURE_CONFIG, Placements.AT_SURFACE_IN_SURFACE_WORLD_CHUNKS_DIVISIBLE_BY_16, new FrequencyConfig(1)));

			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createCompositeFeature(Feature.MINABLE, new MinableConfig(BlockMatcher.forBlock(Blocks.NETHERRACK), Blocks.IRON_ORE.getDefaultState(), 20), Biome.COUNT_RANGE, new CountRangeConfig(16, 10, 0, 118)));
			}

			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.VOID)) {
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createCompositeFeature(Feature.MINABLE, new MinableConfig(BlockMatcher.forBlock(Blocks.END_STONE), Blocks.IRON_ORE.getDefaultState(), 20), Biome.COUNT_RANGE, new CountRangeConfig(16, 0, 0, 128)));
			}
		}
	}
}
