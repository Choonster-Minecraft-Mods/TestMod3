package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Registers this mod's {@link SoundEvent}s.
 *
 * @author Choonster
 */
@SuppressWarnings("WeakerAccess")
public class ModSoundEvents {
	public static final SoundEvent RECORD_SOLARIS;
	public static final SoundEvent NINE_MM_FIRE;
	public static final SoundEvent ACTION_SADDLE;

	static {
		RECORD_SOLARIS = registerSound("record.solaris");
		NINE_MM_FIRE = registerSound("9mm.fire");
		ACTION_SADDLE = registerSound("action.saddle");
	}

	/**
	 * Register the {@link SoundEvent}s.
	 */
	public static void registerSounds() {
		// Dummy method to make sure the static initialiser runs
	}

	/**
	 * Register a {@link SoundEvent}.
	 *
	 * @param soundName The SoundEvent's name without the testmod3 prefix
	 * @return The SoundEvent
	 */
	private static SoundEvent registerSound(String soundName) {
		final ResourceLocation soundID = new ResourceLocation(TestMod3.MODID, soundName);
		return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
	}
}
