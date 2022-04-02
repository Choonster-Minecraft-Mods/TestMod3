package choonster.testmod3.init.levelgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.RegistryUtil;
import choonster.testmod3.world.level.levelgen.placement.InChunksDivisibleBy16Filter;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

/**
 * Registers this mod's {@link PlacedFeature}s.
 *
 * @author Choonster
 */
public class ModPlacedFeatures {
	private static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
			DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, TestMod3.MODID);

	private static boolean isInitialised = false;

	// Places a banner at the surface, but only in chunks with coordinates divisible by 16.
	// Test for this thread:
	// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
	public static final RegistryObject<PlacedFeature> BANNER = register("banner",
			ModConfiguredFeatures.BANNER,
			CountPlacement.of(1),
			InSquarePlacement.spread(),
			PlacementUtils.HEIGHTMAP,
			InChunksDivisibleBy16Filter.instance()
	);

	public static final RegistryObject<PlacedFeature> ORE_IRON_NETHER = register("ore_iron_nether",
			ModConfiguredFeatures.ORE_IRON_NETHER,
			CountPlacement.of(16),
			InSquarePlacement.spread(),
			HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(118))
	);

	public static final RegistryObject<PlacedFeature> ORE_IRON_END = register("ore_iron_end",
			ModConfiguredFeatures.ORE_IRON_END,
			CountPlacement.of(16),
			InSquarePlacement.spread(),
			HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(118))
	);

	/**
	 * Registers the {@link DeferredRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		// Ensure this is run after the ConfiguredFeature registration in ModConfiguredFeatures
		PLACED_FEATURES.<Feature<?>>register(modEventBus);

		isInitialised = true;
	}

	private static <FC extends FeatureConfiguration, F extends Feature<FC>> RegistryObject<PlacedFeature> register(
			final String name,
			final RegistryObject<ConfiguredFeature<FC, F>> feature,
			final PlacementModifier... placements
	) {
		return PLACED_FEATURES.register(
				name,
				() -> new PlacedFeature(
						RegistryUtil.getHolderOrThrow(BuiltinRegistries.CONFIGURED_FEATURE, feature),
						List.of(placements)
				)
		);
	}
}
