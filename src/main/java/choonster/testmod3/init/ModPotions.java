package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.potion.PotionTestMod3;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * Registers this mod's {@link Potion}s.
 *
 * @author Choonster
 */
@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModPotions {

	public static final PotionTestMod3 TEST = Null();

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		/**
		 * Register this mod's {@link Potion}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerPotions(final RegistryEvent.Register<Potion> event) {
			final Potion[] potions = {
					new PotionTestMod3(false, 2, 2, 2, "test"),
			};

			event.getRegistry().registerAll(potions);
		}
	}

}
