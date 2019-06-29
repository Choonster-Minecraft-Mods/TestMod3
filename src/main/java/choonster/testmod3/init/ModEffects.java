package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.potion.TestMod3Effect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * Registers this mod's {@link Effect}s.
 *
 * @author Choonster
 */
@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModEffects {

	public static final TestMod3Effect TEST = Null();

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		/**
		 * Register this mod's {@link Effect}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerPotions(final RegistryEvent.Register<Effect> event) {
			final Effect[] potions = {
					new TestMod3Effect(EffectType.BENEFICIAL, 2, 2, 2).setRegistryName("test"),
			};

			event.getRegistry().registerAll(potions);
		}
	}
}
