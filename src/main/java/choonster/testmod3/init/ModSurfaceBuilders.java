package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.levelgen.surfacebuilders.LoggingSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Registers this mod's {@link SurfaceBuilder}s.
 *
 * @author Choonster
 */
public class ModSurfaceBuilders {
	private static final DeferredRegister<SurfaceBuilder<?>> SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<SurfaceBuilder<SurfaceBuilderBaseConfiguration>> LOGGING_DEFAULT = SURFACE_BUILDERS.register(
			"logging_default",
			() -> new LoggingSurfaceBuilder<>(() -> SurfaceBuilder.DEFAULT, SurfaceBuilderBaseConfiguration.CODEC)
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

		SURFACE_BUILDERS.register(modEventBus);

		isInitialised = true;
	}
}
