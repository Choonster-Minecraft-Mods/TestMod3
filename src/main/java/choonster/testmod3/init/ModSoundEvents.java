package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

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
		private static Map<ResourceLocation, SoundEvent> SOUND_EVENTS;

		/**
		 * Get the {@link SoundEvent} with the specified ID.
		 *
		 * @param soundID The sound event's name
		 * @return The sound event
		 * @throws IllegalStateException If called before the sound events have been initialised
		 * @throws IllegalStateException If the specified sound event doesn't exist
		 */
		public static SoundEvent getSoundEvent(final ResourceLocation soundID) {
			Preconditions.checkState(SOUND_EVENTS != null, "Attempt to get Sound Events before initialisation");
			Preconditions.checkState(SOUND_EVENTS.containsKey(soundID), "Attempt to get non-existent Sound Event %s", soundID);

			return SOUND_EVENTS.get(soundID);
		}

		/**
		 * Initialise this mod's {@link SoundEvent}s.
		 * <p>
		 * This needs to be done before {@link RegistryEvent.Register<SoundEvent>} is fired so that the sound events
		 * can be used by record item constructors in {@link RegistryEvent.Register<Item>}.
		 */
		public static void initialiseSoundEvents() {
			Preconditions.checkState(SOUND_EVENTS == null, "Attempt to re-initialise Sound Events");

			SOUND_EVENTS = new ImmutableMap.Builder<ResourceLocation, SoundEvent>()
					.put(createSoundEvent("record.solaris"))
					.put(createSoundEvent("9mm.fire"))
					.put(createSoundEvent("action.saddle"))
					.build();
		}

		/**
		 * Register this mod's {@link SoundEvent}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
			Preconditions.checkState(SOUND_EVENTS != null, "Sound Events weren't initialised before registration");
			SOUND_EVENTS.values().forEach(event.getRegistry()::register);
		}

		/**
		 * Create a {@link SoundEvent}.
		 *
		 * @param soundName The sound event's name without the testmod3 prefix
		 * @return A pair of the sound event's ID and the sound event itself
		 */
		private static Map.Entry<ResourceLocation, SoundEvent> createSoundEvent(final String soundName) {
			final ResourceLocation soundID = new ResourceLocation(TestMod3.MODID, soundName);
			final SoundEvent soundEvent = new SoundEvent(soundID).setRegistryName(soundID);
			return Pair.of(soundID, soundEvent);
		}
	}
}
