package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 * Registers this mod's {@link SoundEvent}s.
 *
 * @author Choonster
 */
@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModSoundEvents {

	@ObjectHolder("record.solaris")
	public static final SoundEvent RECORD_SOLARIS = createSoundEvent("record.solaris");

	@ObjectHolder("9mm.fire")
	public static final SoundEvent NINE_MM_FIRE = createSoundEvent("9mm.fire");

	@ObjectHolder("action.saddle")
	public static final SoundEvent ACTION_SADDLE = createSoundEvent("action.saddle");

	/**
	 * Create a {@link SoundEvent}.
	 *
	 * @param soundName The SoundEvent's name without the testmod3 prefix
	 * @return The SoundEvent
	 */
	private static SoundEvent createSoundEvent(String soundName) {
		final ResourceLocation soundID = new ResourceLocation(TestMod3.MODID, soundName);
		return new SoundEvent(soundID).setRegistryName(soundID);
	}

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
			event.getRegistry().registerAll(
					RECORD_SOLARIS,
					NINE_MM_FIRE,
					ACTION_SADDLE
			);
		}
	}
}
