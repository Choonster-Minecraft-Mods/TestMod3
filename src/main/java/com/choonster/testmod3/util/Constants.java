package com.choonster.testmod3.util;

import com.choonster.testmod3.TestMod3;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constants {
	public static final String RESOURCE_PREFIX = TestMod3.MODID + ":";

	/**
	 * The armour equipment slots.
	 */
	public static final Set<EntityEquipmentSlot> ARMOUR_SLOTS = ImmutableSet.copyOf(
			Stream.of(EntityEquipmentSlot.values())
					.filter(equipmentSlot -> equipmentSlot.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
					.collect(Collectors.toList())
	);
}
