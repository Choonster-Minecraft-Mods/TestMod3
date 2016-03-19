package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import com.google.common.collect.ImmutableList;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

import java.util.List;

/**
 * Base class for this mod's armour items
 *
 * @author Choonster
 */
public class ItemArmourTestMod3 extends ItemArmor {
	/**
	 * {@link #armorType} to slot name mapping
	 */
	private static final List<String> SLOT_NAMES = ImmutableList.of("helmet", "chestplate", "leggings", "boots");

	public ItemArmourTestMod3(final ArmorMaterial material, final EntityEquipmentSlot equipmentSlot, final String armourName) {
		super(material, -1, equipmentSlot);
		ItemTestMod3.setItemName(this,  equipmentSlot.getName() + armourName);
		setCreativeTab(TestMod3.creativeTab);
	}
}
