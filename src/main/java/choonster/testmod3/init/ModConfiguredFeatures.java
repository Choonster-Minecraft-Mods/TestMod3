package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.gen.feature.BannerFeatureConfig;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * Registers this mod's {@link ConfiguredFeature}s.
 *
 * @author Choonster
 */
public class ModConfiguredFeatures {
	public static final RegistryKey<ConfiguredFeature<?, ?>> BANNER = key("banner");
	public static final RegistryKey<ConfiguredFeature<?, ?>> NETHER_IRON_ORE = key("nether_iron_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> END_IRON_ORE = key("end_iron_ore");

	private static RegistryKey<ConfiguredFeature<?, ?>> key(final String name) {
		return RegistryKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, new ResourceLocation(TestMod3.MODID, name));
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		// Ensure this is run after the Feature DeferredRegister in ModFeatures
		@SubscribeEvent(priority = EventPriority.LOW)
		public static void register(final RegistryEvent.Register<Feature<?>> event) {
			// Places a banner at the surface, but only in chunks with coordinates divisible by 16.
			// Test for this thread:
			// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
			register(BANNER,
					ModFeatures.BANNER.get()
							.configured(BannerFeatureConfig.create(
									DyeColor.PINK,
									Pair.of(BannerPattern.GRADIENT_UP, DyeColor.MAGENTA),
									Pair.of(BannerPattern.FLOWER, DyeColor.BLACK)
							))
							.decorated(Features.Placements.HEIGHTMAP_SQUARE)
							.decorated(ModConfiguredPlacements.IN_CHUNKS_DIVISIBLE_BY_16.get())
							.count(1)
			);

			register(NETHER_IRON_ORE,
					Feature.ORE
							.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, Blocks.IRON_ORE.defaultBlockState(), 20))
							.range(118)
							.squared()
							.count(16)
			);

			register(END_IRON_ORE,
					Feature.ORE
							.configured(new OreFeatureConfig(FillerBlockType.END_STONE, Blocks.IRON_ORE.defaultBlockState(), 20))
							.range(128)
							.squared()
							.count(16)
			);
		}

		private static void register(final RegistryKey<ConfiguredFeature<?, ?>> key, final ConfiguredFeature<?, ?> configuredFeature) {
			Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key.location(), configuredFeature);
		}
	}

	public static class FillerBlockType {
		public static final RuleTest END_STONE = new BlockMatchRuleTest(Blocks.END_STONE);
	}
}
