package choonster.testmod3.item;

import choonster.testmod3.util.StringUtils;
import net.minecraft.item.Item;

/**
 * An item that displays a number stored in NBT as a superscript in its display name.
 *
 * @author Choonster
 */
public class ItemWithSuperscripts extends ItemWithScripts {
	public ItemWithSuperscripts(final Item.Properties properties) {
		super(StringUtils::superscript, properties);
	}
}
