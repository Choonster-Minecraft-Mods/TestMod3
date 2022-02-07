package choonster.testmod3.init.levelgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.registry.DeferredVanillaRegister;
import choonster.testmod3.registry.VanillaRegistryObject;
import choonster.testmod3.world.level.levelgen.feature.BannerFeatureConfig;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.List;

/**
 * Registers this mod's {@link ConfiguredFeature}s.
 *
 * @author Choonster
 */
public class ModConfiguredFeatures {
	private static final DeferredVanillaRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
			DeferredVanillaRegister.create(BuiltinRegistries.CONFIGURED_FEATURE, TestMod3.MODID);

	private static boolean isInitialised = false;

	/**
	 * Places a banner at the surface, but only in chunks with coordinates divisible by 16.
	 * Test for this thread:
	 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
	 */
	public static final VanillaRegistryObject<ConfiguredFeature<?, ?>> BANNER = CONFIGURED_FEATURES.register("banner",
			() -> ModFeatures.BANNER.get().configured(BannerFeatureConfig.create(
					DyeColor.PINK,
					Pair.of(BannerPattern.GRADIENT_UP, DyeColor.MAGENTA),
					Pair.of(BannerPattern.FLOWER, DyeColor.BLACK)
			))
	);

	public static final VanillaRegistryObject<ConfiguredFeature<?, ?>> ORE_IRON_NETHER = CONFIGURED_FEATURES.register("ore_iron_nether",
			() -> Feature.ORE.configured(new OreConfiguration(
					List.of(OreConfiguration.target(
							OreFeatures.NETHER_ORE_REPLACEABLES,
							Blocks.IRON_ORE.defaultBlockState()
					)),
					9
			))
	);

	public static final VanillaRegistryObject<ConfiguredFeature<?, ?>> ORE_IRON_END = CONFIGURED_FEATURES.register("ore_iron_end",
			() -> Feature.ORE.configured(new OreConfiguration(
					List.of(OreConfiguration.target(
							new BlockMatchTest(Blocks.END_STONE),
							Blocks.IRON_ORE.defaultBlockState()
					)),
					9
			))
	);

	/**
	 * Registers the {@link DeferredVanillaRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		CONFIGURED_FEATURES.register(modEventBus);

		isInitialised = true;
	}
}
