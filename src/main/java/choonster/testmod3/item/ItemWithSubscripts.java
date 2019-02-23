package choonster.testmod3.item;

import choonster.testmod3.util.StringUtils;

/**
 * An item that displays a number stored in NBT as a subscript in its display name.
 *
 * @author Choonster
 */
public class ItemWithSubscripts extends ItemWithScripts {
	public ItemWithSubscripts() {
		super(StringUtils::subscript);
	}
}
