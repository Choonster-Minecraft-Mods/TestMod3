package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.gen.placement.AtSurfaceInChunksDivisibleBy16;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * Registers this mod's {@link Placement}s.
 *
 * @author Choonster
 */
@ObjectHolder(TestMod3.MODID)
public class ModPlacements {
	public static final Placement<FeatureSpreadConfig> AT_SURFACE_IN_CHUNKS_DIVISIBLE_BY_16 = Null();

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void registerPlacements(final RegistryEvent.Register<Placement<?>> event) {
			event.getRegistry().registerAll(
					new AtSurfaceInChunksDivisibleBy16(FeatureSpreadConfig.field_242797_a).setRegistryName("at_surface_in_surface_world_chunks_divisible_by_16")
			);
		}
	}
}
