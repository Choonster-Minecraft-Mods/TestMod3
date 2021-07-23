package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
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
	public static final ResourceKey<ConfiguredSurfaceBuilder<?>> DESERT_TEST = key("desert_test");

	private static ResourceKey<ConfiguredSurfaceBuilder<?>> key(final String name) {
		return ResourceKey.create(Registry.CONFIGURED_SURFACE_BUILDER_REGISTRY, new ResourceLocation(TestMod3.MODID, name));
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		// Ensure this is run after the SurfaceBuilder DeferredRegister in ModSurfaceBuilders
		@SubscribeEvent(priority = EventPriority.LOW)
		public static void register(final RegistryEvent.Register<SurfaceBuilder<?>> event) {
			register(DESERT_TEST,
					ModSurfaceBuilders.LOGGING_DEFAULT.get()
							.configured(new SurfaceBuilderBaseConfiguration(Blocks.RED_SAND.defaultBlockState(), Blocks.BRICKS.defaultBlockState(), Blocks.GRAVEL.defaultBlockState()))
			);
		}

		private static void register(final ResourceKey<ConfiguredSurfaceBuilder<?>> key, final ConfiguredSurfaceBuilder<?> configuredSurfaceBuilder) {
			Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, key.location(), configuredSurfaceBuilder);
		}
	}
}
