package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.JukeboxSong;

/**
 * Stores the keys for this mod's {@link net.minecraft.world.item.JukeboxSong JukeboxSongs}.
 *
 * @author Choonster
 */
public class ModJukeboxSongs {
	public static final ResourceKey<JukeboxSong> SOLARIS = key("solaris");

	private static ResourceKey<JukeboxSong> key(String name) {
		return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(TestMod3.MODID, name));
	}
}
