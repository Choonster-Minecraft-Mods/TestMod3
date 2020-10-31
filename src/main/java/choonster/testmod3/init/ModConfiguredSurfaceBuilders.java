package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.gen.surfacebuilders.LoggingConfiguredSurfaceBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

/**
 * Registers this mod's {@link ConfiguredSurfaceBuilder}s.
 *
 * @author Choonster
 */
public class ModConfiguredSurfaceBuilders {
	public static RegistryKey<ConfiguredSurfaceBuilder<?>> DESERT_TEST = key("desert_test");

	public static void register() {
		register(DESERT_TEST,
				new LoggingConfiguredSurfaceBuilder<>(
						SurfaceBuilder.DEFAULT,
						new SurfaceBuilderConfig(Blocks.RED_SAND.getDefaultState(), Blocks.BRICKS.getDefaultState(), Blocks.GRAVEL.getDefaultState())
				)
		);
	}

	private static RegistryKey<ConfiguredSurfaceBuilder<?>> key(final String name) {
		return RegistryKey.getOrCreateKey(Registry.CONFIGURED_SURFACE_BUILDER_KEY, new ResourceLocation(TestMod3.MODID, name));
	}

	private static void register(final RegistryKey<ConfiguredSurfaceBuilder<?>> key, final ConfiguredSurfaceBuilder<?> configuredSurfaceBuilder) {
		Registry.register(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, key.getLocation(), configuredSurfaceBuilder);
	}
}
