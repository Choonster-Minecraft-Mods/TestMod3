package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.levelgen.placement.InChunksDivisibleBy16;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Registers this mod's {@link FeatureDecorator}s.
 *
 * @author Choonster
 */
public class ModFeatureDecorators {
	private static final DeferredRegister<FeatureDecorator<?>> FEATURE_DECORATORS = DeferredRegister.create(ForgeRegistries.DECORATORS, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<InChunksDivisibleBy16> IN_CHUNKS_DIVISIBLE_BY_16 = FEATURE_DECORATORS.register("in_chunks_divisible_by_16",
			() -> new InChunksDivisibleBy16(NoneDecoratorConfiguration.CODEC)
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

		FEATURE_DECORATORS.register(modEventBus);

		isInitialised = true;
	}
}
