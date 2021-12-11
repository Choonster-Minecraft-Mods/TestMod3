package choonster.testmod3.init.levelgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.levelgen.feature.BannerFeatureConfig;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.List;

/**
 * Registers this mod's {@link ConfiguredFeature}s.
 *
 * @author Choonster
 */
public class ModConfiguredFeatures {
	public static final ResourceKey<ConfiguredFeature<?, ?>> BANNER = key("banner");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON_NETHER = key("ore_iron_nether");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON_END = key("ore_iron_end");

	private static ResourceKey<ConfiguredFeature<?, ?>> key(final String name) {
		return ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, new ResourceLocation(TestMod3.MODID, name));
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		// Ensure this is run after the Feature DeferredRegister in ModFeatures
		@SubscribeEvent(priority = EventPriority.LOW)
		public static void register(final RegistryEvent.Register<Feature<?>> event) {
			final BlockMatchTest endStone = new BlockMatchTest(Blocks.END_STONE);

			final List<OreConfiguration.TargetBlockState> oreIronNetherTargetList = List.of(OreConfiguration.target(
					OreFeatures.NETHER_ORE_REPLACEABLES,
					Blocks.IRON_ORE.defaultBlockState()
			));

			final List<OreConfiguration.TargetBlockState> oreIronEndTargetList = List.of(OreConfiguration.target(
					endStone,
					Blocks.IRON_ORE.defaultBlockState()
			));

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
			);

			register(ORE_IRON_NETHER, Feature.ORE.configured(new OreConfiguration(oreIronNetherTargetList, 9)));

			register(ORE_IRON_END, Feature.ORE.configured(new OreConfiguration(oreIronEndTargetList, 9)));
		}

		private static void register(final ResourceKey<ConfiguredFeature<?, ?>> key, final ConfiguredFeature<?, ?> configuredFeature) {
			Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, key.location(), configuredFeature);
		}
	}
}
