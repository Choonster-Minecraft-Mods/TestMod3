package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.levelgen.feature.BannerFeatureConfig;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
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
	public static final ResourceKey<ConfiguredFeature<?, ?>> BANNER = key("banner");
	public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_IRON_ORE = key("nether_iron_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> END_IRON_ORE = key("end_iron_ore");

	private static ResourceKey<ConfiguredFeature<?, ?>> key(final String name) {
		return ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, new ResourceLocation(TestMod3.MODID, name));
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
							.decorated(Features.Decorators.HEIGHTMAP_SQUARE)
							.decorated(ModConfiguredDecorators.IN_CHUNKS_DIVISIBLE_BY_16.get())
							.count(1)
			);

			register(NETHER_IRON_ORE,
					Feature.ORE
							.configured(new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, Blocks.IRON_ORE.defaultBlockState(), 20))
							.rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(118))
							.squared()
							.count(16)
			);

			register(END_IRON_ORE,
					Feature.ORE
							.configured(new OreConfiguration(FillerBlockType.END_STONE, Blocks.IRON_ORE.defaultBlockState(), 20))
							.rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(128))
							.squared()
							.count(16)
			);
		}

		private static void register(final ResourceKey<ConfiguredFeature<?, ?>> key, final ConfiguredFeature<?, ?> configuredFeature) {
			Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, key.location(), configuredFeature);
		}
	}

	public static class FillerBlockType {
		public static final RuleTest END_STONE = new BlockMatchTest(Blocks.END_STONE);
	}
}
