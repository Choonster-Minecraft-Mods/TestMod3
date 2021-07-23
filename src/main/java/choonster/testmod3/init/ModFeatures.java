package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.levelgen.feature.BannerFeature;
import choonster.testmod3.world.level.levelgen.feature.BannerFeatureConfig;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Registers this mod's {@link Feature}s.
 *
 * @author Choonster
 */
public class ModFeatures {
	private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<BannerFeature> BANNER = FEATURES.register("banner",
			() -> new BannerFeature(BannerFeatureConfig.CODEC)
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

		FEATURES.register(modEventBus);

		isInitialised = true;
	}
}
