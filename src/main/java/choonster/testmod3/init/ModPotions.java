package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.potion.PotionTestMod3;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 * Registers this mod's {@link Potion}s.
 *
 * @author Choonster
 */
@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModPotions {

	public static final PotionTestMod3 TEST = new PotionTestMod3(false, 2, 2, 2, "test");

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {
		/**
		 * Register this mod's {@link Potion}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerPotions(final RegistryEvent.Register<Potion> event) {
			event.getRegistry().registerAll(
					TEST
			);
		}
	}

}
