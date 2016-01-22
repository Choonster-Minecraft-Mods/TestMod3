package com.choonster.testmod3.util;

import com.choonster.testmod3.TestMod3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;

public class Constants {
	public static final String RESOURCE_PREFIX = TestMod3.MODID + ":";

	/**
	 * Values for {@link ItemArmor#armorType}
	 */
	public static final int ARMOUR_TYPE_HEAD = 0, ARMOUR_TYPE_CHEST = 1, ARMOUR_TYPE_LEGS = 2, ARMOUR_TYPE_FEET = 3;

	/**
	 * Values for {@link Entity#setCurrentItemOrArmor} and {@link EntityLivingBase#getEquipmentInSlot}
	 */
	public static final int ITEM_SLOT_HEAD = 4, ITEM_SLOT_CHEST = 3, ITEM_SLOT_LEGS = 2, ITEM_SLOT_FEET = 1, ITEM_SLOT_HAND = 0;
}
