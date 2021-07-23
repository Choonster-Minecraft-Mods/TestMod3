package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * Registers this mod's {@link ConfiguredSurfaceBuilder}s.
 *
 * @author Choonster
 */
public class ModConfiguredSurfaceBuilders {
	public static final RegistryKey<ConfiguredSurfaceBuilder<?>> DESERT_TEST = key("desert_test");

	private static RegistryKey<ConfiguredSurfaceBuilder<?>> key(final String name) {
		return RegistryKey.create(Registry.CONFIGURED_SURFACE_BUILDER_REGISTRY, new ResourceLocation(TestMod3.MODID, name));
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		// Ensure this is run after the SurfaceBuilder DeferredRegister in ModSurfaceBuilders
		@SubscribeEvent(priority = EventPriority.LOW)
		public static void register(final RegistryEvent.Register<SurfaceBuilder<?>> event) {
			register(DESERT_TEST,
					ModSurfaceBuilders.LOGGING_DEFAULT.get()
							.configured(new SurfaceBuilderConfig(Blocks.RED_SAND.defaultBlockState(), Blocks.BRICKS.defaultBlockState(), Blocks.GRAVEL.defaultBlockState()))
			);
		}

		private static void register(final RegistryKey<ConfiguredSurfaceBuilder<?>> key, final ConfiguredSurfaceBuilder<?> configuredSurfaceBuilder) {
			Registry.register(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, key.location(), configuredSurfaceBuilder);
		}
	}
}
