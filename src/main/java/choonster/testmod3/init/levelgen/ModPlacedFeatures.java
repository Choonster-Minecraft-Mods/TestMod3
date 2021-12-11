package choonster.testmod3.init.levelgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.levelgen.placement.InChunksDivisibleBy16Filter;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Registers this mod's {@link PlacedFeature}s.
 *
 * @author Choonster
 */
public class ModPlacedFeatures {
	public static final ResourceKey<PlacedFeature> BANNER = key("banner");
	public static final ResourceKey<PlacedFeature> ORE_IRON_NETHER = key("ore_iron_nether");
	public static final ResourceKey<PlacedFeature> ORE_IRON_END = key("ore_iron_end");

	private static ResourceKey<PlacedFeature> key(final String name) {
		return ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(TestMod3.MODID, name));
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler {
		// Ensure this is run after the ConfiguredFeature registration in ModConfiguredFeatures
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void register(final RegistryEvent.Register<Feature<?>> event) {
			// Places a banner at the surface, but only in chunks with coordinates divisible by 16.
			// Test for this thread:
			// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
			register(BANNER,
					feature(ModConfiguredFeatures.BANNER).placed(
							CountPlacement.of(1),
							InSquarePlacement.spread(),
							PlacementUtils.HEIGHTMAP,
							InChunksDivisibleBy16Filter.instance()
					)
			);

			register(ORE_IRON_NETHER,
					feature(ModConfiguredFeatures.ORE_IRON_NETHER).placed(
							CountPlacement.of(16),
							InSquarePlacement.spread(),
							HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(118))
					)
			);

			register(ORE_IRON_END,
					feature(ModConfiguredFeatures.ORE_IRON_END).placed(
							CountPlacement.of(16),
							InSquarePlacement.spread(),
							HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(118))
					)
			);
		}

		private static void register(final ResourceKey<PlacedFeature> key, final PlacedFeature placedFeature) {
			Registry.register(BuiltinRegistries.PLACED_FEATURE, key.location(), placedFeature);
		}

		private static ConfiguredFeature<?, ?> feature(final ResourceKey<ConfiguredFeature<?, ?>> key) {
			return BuiltinRegistries.CONFIGURED_FEATURE.getOrThrow(key);
		}
	}
}
