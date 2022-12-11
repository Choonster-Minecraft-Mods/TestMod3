package choonster.testmod3.data.worldgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.levelgen.placement.InChunksDivisibleBy16Filter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

/**
 * Registers this mod's {@link PlacedFeature}s during datagen.
 *
 * @author Choonster
 */
public class ModPlacedFeatures {
	// Places a banner at the surface, but only in chunks with coordinates divisible by 16.
	// Test for this thread:
	// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
	public static final ResourceKey<PlacedFeature> BANNER = key("banner");
	public static final ResourceKey<PlacedFeature> ORE_IRON_NETHER = key("ore_iron_nether");
	public static final ResourceKey<PlacedFeature> ORE_IRON_END = key("ore_iron_end");

	public static void bootstrap(final BootstapContext<PlacedFeature> context) {
		register(context, BANNER, ModConfiguredFeatures.BANNER,
				CountPlacement.of(1),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP,
				InChunksDivisibleBy16Filter.instance()
		);

		register(context, ORE_IRON_NETHER, ModConfiguredFeatures.ORE_IRON_NETHER,
				CountPlacement.of(16),
				InSquarePlacement.spread(),
				HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(118))
		);

		register(context, ORE_IRON_END, ModConfiguredFeatures.ORE_IRON_END,
				CountPlacement.of(16),
				InSquarePlacement.spread(),
				HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(118))
		);
	}

	private static void register(
			final BootstapContext<PlacedFeature> context,
			final ResourceKey<PlacedFeature> key,
			final ResourceKey<ConfiguredFeature<?, ?>> feature,
			final PlacementModifier... placement
	) {
		PlacementUtils.register(context, key, context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(feature), placement);
	}

	private static ResourceKey<PlacedFeature> key(final String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(TestMod3.MODID, name));
	}
}
