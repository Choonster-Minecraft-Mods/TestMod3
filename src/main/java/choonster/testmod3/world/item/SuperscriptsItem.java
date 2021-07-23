package choonster.testmod3.world.item;

import choonster.testmod3.util.StringUtils;
import net.minecraft.world.item.Item;

/**
 * An item that displays a number stored in NBT as a superscript in its display name.
 *
 * @author Choonster
 */
public class SuperscriptsItem extends ScriptsItem {
	public SuperscriptsItem(final Item.Properties properties) {
		super(StringUtils::superscript, properties);
	}
}
