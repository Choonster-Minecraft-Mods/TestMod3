package choonster.testmod3.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;

/**
 * A record.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2408066-try-creating-a-music-disc-in-my-1-8-mod-please
 *
 * @author Choonster
 */
public class ItemRecordMod extends ItemRecord {
	public ItemRecordMod(final int comparatorValue, final SoundEvent soundIn, final Item.Properties builder) {
		super(comparatorValue, soundIn, builder);
	}
}
