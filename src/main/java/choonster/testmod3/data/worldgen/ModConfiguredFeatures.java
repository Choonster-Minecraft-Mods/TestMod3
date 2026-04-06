package choonster.testmod3.data.worldgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.levelgen.ModFeatures;
import choonster.testmod3.world.level.levelgen.feature.BannerFeatureConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
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

	public static void bootstrap(final BootstrapContext<ConfiguredFeature<?, ?>> context) {
		final var patterns = context.lookup(Registries.BANNER_PATTERN);


		register(context, BANNER, ModFeatures.BANNER.get(),
				new BannerFeatureConfig(
						BlockStateProvider.simple(Blocks.PINK_BANNER),
						new BannerPatternLayers.Builder()
								.add(pattern(patterns, BannerPatterns.GRADIENT_UP), DyeColor.MAGENTA)
								.add(pattern(patterns, BannerPatterns.FLOWER), DyeColor.BLACK)
								.build()
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

	private static Holder<BannerPattern> pattern(final HolderGetter<BannerPattern> lookup, final ResourceKey<BannerPattern> key) {
		return lookup.getOrThrow(key);
	}

	private static ResourceKey<ConfiguredFeature<?, ?>> key(final String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(TestMod3.MODID, name));
	}
}
