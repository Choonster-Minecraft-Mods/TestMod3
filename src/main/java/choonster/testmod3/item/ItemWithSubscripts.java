package choonster.testmod3.item;

import choonster.testmod3.util.StringUtils;
import net.minecraft.item.Item;

/**
 * An item that displays a number stored in NBT as a subscript in its display name.
 *
 * @author Choonster
 */
public class ItemWithSubscripts extends ItemWithScripts {
	public ItemWithSubscripts(final Item.Properties properties) {
		super(StringUtils::subscript, properties);
	}
}
