package choonster.testmod3.item;

import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.SoundEvent;

/**
 * A music disc.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2408066-try-creating-a-music-disc-in-my-1-8-mod-please
 *
 * @author Choonster
 */
public class ModMusicDiscItem extends MusicDiscItem {
	public ModMusicDiscItem(final int comparatorValue, final SoundEvent soundIn, final Item.Properties builder) {
		super(comparatorValue, soundIn, builder);
	}
}
