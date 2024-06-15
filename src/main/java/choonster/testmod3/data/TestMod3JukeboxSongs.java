package choonster.testmod3.data;

import choonster.testmod3.init.ModJukeboxSongs;
import choonster.testmod3.init.ModSoundEvents;
import net.minecraft.Util;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers this mod's {@link JukeboxSong JukeboxSongs} during datagen.
 *
 * @author Choonster
 */
public class TestMod3JukeboxSongs {
	public static void bootstrap(final BootstrapContext<JukeboxSong> context) {
		register(context, ModJukeboxSongs.SOLARIS, ModSoundEvents.MUSIC_DISC_SOLARIS, 454.25f, 13);
	}

	private static void register(
			final BootstrapContext<JukeboxSong> context,
			final ResourceKey<JukeboxSong> key,
			final RegistryObject<SoundEvent> soundEvent,
			final float lengthInSeconds,
			final int comparatorOutput
	) {
		context.register(
				key,
				new JukeboxSong(soundEvent.getHolder().orElseThrow(), Component.translatable(Util.makeDescriptionId("jukebox_song", key.location())), lengthInSeconds, comparatorOutput)
		);
	}
}
