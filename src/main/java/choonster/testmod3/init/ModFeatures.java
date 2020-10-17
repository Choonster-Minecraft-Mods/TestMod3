package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.gen.feature.BannerFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * @author Choonster
 */
@ObjectHolder(TestMod3.MODID)
public class ModFeatures {
	public static final Feature<NoFeatureConfig> BANNER = Null();

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void registerFeatures(final RegistryEvent.Register<Feature<?>> event) {
			event.getRegistry().registerAll(
					new BannerFeature(NoFeatureConfig.field_236558_a_).setRegistryName("banner")
			);
		}
	}
}
