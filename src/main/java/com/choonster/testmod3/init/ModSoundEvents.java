package com.choonster.testmod3.init;

import com.choonster.testmod3.TestMod3;
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
	public static SoundEvent record_solaris;
	public static SoundEvent NINE_MM_FIRE;

	/**
	 * Register the {@link SoundEvent}s.
	 */
	public static void registerSounds() {
		record_solaris = registerSound("record.solaris");
		NINE_MM_FIRE = registerSound("9mm.fire");
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
