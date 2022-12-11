package choonster.testmod3.data.worldgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.levelgen.ModFeatures;
import choonster.testmod3.world.level.levelgen.feature.BannerFeatureConfig;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;

import java.util.List;

import static net.minecraft.data.worldgen.features.FeatureUtils.register;

/**
 * Registers this mod's {@link ConfiguredFeature}s during datagen.
 *
 * @author Choonster
 */
public class ModConfiguredFeatures {
	/**
	 * Places a banner at the surface, but only in chunks with coordinates divisible by 16.
	 * Test for this thread:
	 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
	 */
	public static final ResourceKey<ConfiguredFeature<?, ?>> BANNER = key("banner");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON_NETHER = key("ore_iron_nether");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON_END = key("ore_iron_end");

	public static void bootstrap(final BootstapContext<ConfiguredFeature<?, ?>> context) {
		register(context, BANNER, ModFeatures.BANNER.get(),
				BannerFeatureConfig.create(
						DyeColor.PINK,
						Pair.of(BannerPatterns.GRADIENT_UP, DyeColor.MAGENTA),
						Pair.of(BannerPatterns.FLOWER, DyeColor.BLACK)
				)
		);

		register(context, ORE_IRON_NETHER, Feature.ORE,
				new OreConfiguration(
						List.of(OreConfiguration.target(
								new BlockMatchTest(Blocks.NETHERRACK),
								Blocks.IRON_ORE.defaultBlockState()
						)),
						9
				)
		);

		register(context, ORE_IRON_END, Feature.ORE,
				new OreConfiguration(
						List.of(OreConfiguration.target(
								new BlockMatchTest(Blocks.END_STONE),
								Blocks.IRON_ORE.defaultBlockState()
						)),
						9
				)
		);
	}

	private static ResourceKey<ConfiguredFeature<?, ?>> key(final String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(TestMod3.MODID, name));
	}
}
