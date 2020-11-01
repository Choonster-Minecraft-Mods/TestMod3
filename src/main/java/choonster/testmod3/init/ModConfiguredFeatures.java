package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;
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
		return RegistryKey.getOrCreateKey(Registry.CONFIGURED_FEATURE_KEY, new ResourceLocation(TestMod3.MODID, name));
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		// Ensure this is run after the Feature DeferredRegister in ModFeatures
		@SubscribeEvent(priority = EventPriority.LOW)
		public static void register(final RegistryEvent.Register<Feature<?>> event) {
			register(BANNER,
					ModFeatures.BANNER.get()
							.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
							.withPlacement(ModPlacements.AT_SURFACE_IN_CHUNKS_DIVISIBLE_BY_16.get().configure(new FeatureSpreadConfig(1)))
			);

			register(NETHER_IRON_ORE,
					Feature.ORE
							.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, Blocks.IRON_ORE.getDefaultState(), 20))
							.range(118)
							.square()
							.func_242731_b(16) // count?
			);

			register(END_IRON_ORE,
					Feature.ORE
							.withConfiguration(new OreFeatureConfig(FillerBlockType.END_STONE, Blocks.IRON_ORE.getDefaultState(), 20))
							.range(128)
							.square()
							.func_242731_b(16) // count?
			);
		}

		private static void register(final RegistryKey<ConfiguredFeature<?, ?>> key, final ConfiguredFeature<?, ?> configuredFeature) {
			Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key.getLocation(), configuredFeature);
		}
	}

	public static class FillerBlockType {
		public static final RuleTest END_STONE = new BlockMatchRuleTest(Blocks.END_STONE);
	}
}
