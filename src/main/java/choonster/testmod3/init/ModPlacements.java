package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.gen.placement.AtSurfaceInChunksDivisibleBy16;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Registers this mod's {@link Placement}s.
 *
 * @author Choonster
 */
public class ModPlacements {
	private static final DeferredRegister<Placement<?>> PLACEMENTS = DeferredRegister.create(ForgeRegistries.DECORATORS, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<AtSurfaceInChunksDivisibleBy16> AT_SURFACE_IN_CHUNKS_DIVISIBLE_BY_16 = PLACEMENTS.register("at_surface_in_chunks_divisible_by_16",
			() -> new AtSurfaceInChunksDivisibleBy16(FeatureSpreadConfig.CODEC)
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

		PLACEMENTS.register(modEventBus);

		isInitialised = true;
	}
}
