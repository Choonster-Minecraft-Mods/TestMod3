package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

/**
 * Base class for this mod's armour items
 *
 * @author Choonster
 */
public class ItemArmourTestMod3 extends ItemArmor {

	public ItemArmourTestMod3(final ArmorMaterial material, final EntityEquipmentSlot equipmentSlot, final String armourName) {
		super(material, -1, equipmentSlot);
		ItemTestMod3.setItemName(this, equipmentSlot.getName() + armourName);
		setCreativeTab(TestMod3.creativeTab);
	}
}
