package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * Registers this mod's {@link SoundEvent}s.
 *
 * @author Choonster
 */
@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModSoundEvents {

	@ObjectHolder("record.solaris")
	public static final SoundEvent RECORD_SOLARIS = Null();

	@ObjectHolder("9mm.fire")
	public static final SoundEvent NINE_MM_FIRE = Null();

	@ObjectHolder("action.saddle")
	public static final SoundEvent ACTION_SADDLE = Null();


	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class RegistrationHandler {
		/**
		 * Register this mod's {@link SoundEvent}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
			final SoundEvent[] soundEvents = {
					createSoundEvent("record.solaris"),
					createSoundEvent("9mm.fire"),
					createSoundEvent("action.saddle"),
			};

			event.getRegistry().registerAll(soundEvents);
		}

		/**
		 * Create a {@link SoundEvent}.
		 *
		 * @param soundName The SoundEvent's name without the testmod3 prefix
		 * @return The SoundEvent
		 */
		private static SoundEvent createSoundEvent(final String soundName) {
			final ResourceLocation soundID = new ResourceLocation(TestMod3.MODID, soundName);
			return new SoundEvent(soundID).setRegistryName(soundID);
		}
	}
}
