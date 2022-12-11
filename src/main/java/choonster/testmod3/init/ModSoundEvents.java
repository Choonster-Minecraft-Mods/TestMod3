package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers this mod's {@link SoundEvent}s.
 *
 * @author Choonster
 */
public class ModSoundEvents {
	private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<SoundEvent> RECORD_SOLARIS = registerSoundEvent("record.solaris");

	public static final RegistryObject<SoundEvent> NINE_MM_FIRE = registerSoundEvent("9mm.fire");

	public static final RegistryObject<SoundEvent> ACTION_SADDLE = registerSoundEvent("action.saddle");


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

		SOUND_EVENTS.register(modEventBus);

		isInitialised = true;
	}

	/**
	 * Registers a sound event.
	 *
	 * @param soundName The sound event's name, without the testmod3 prefix
	 * @return A RegistryObject reference to the SoundEvent
	 */
	private static RegistryObject<SoundEvent> registerSoundEvent(final String soundName) {
		return SOUND_EVENTS.register(soundName, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(TestMod3.MODID, soundName)));
	}
}
