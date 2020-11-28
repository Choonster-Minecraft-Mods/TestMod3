package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.gen.placement.InChunksDivisibleBy16;
import net.minecraft.world.gen.placement.NoPlacementConfig;
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

	public static final RegistryObject<InChunksDivisibleBy16> IN_CHUNKS_DIVISIBLE_BY_16 = PLACEMENTS.register("in_chunks_divisible_by_16",
			() -> new InChunksDivisibleBy16(NoPlacementConfig.CODEC)
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
