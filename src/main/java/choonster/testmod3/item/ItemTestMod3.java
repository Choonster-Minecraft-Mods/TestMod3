package choonster.testmod3.item;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.RegistryUtil;
import net.minecraft.item.Item;

/**
 * A base class for this mod's items.
 *
 * @author Choonster
 */
public class ItemTestMod3 extends Item {
	public ItemTestMod3(final String itemName) {
		RegistryUtil.setItemName(this, itemName);
		setCreativeTab(TestMod3.creativeTab);
	}

}
