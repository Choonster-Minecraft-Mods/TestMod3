package com.choonster.testmod3.util;

import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * Utility methods for inventories.
 *
 * @author Choonster
 */
public class InventoryUtils {

	/**
	 * Get the {@link EntityEquipmentSlot} with the specified index (as returned by {@link EntityEquipmentSlot#func_188452_c()}.
	 *
	 * @param index The index
	 * @return The equipment slot
	 */
	public static EntityEquipmentSlot getEquipmentSlotFromIndex(int index) {
		for (EntityEquipmentSlot equipmentSlot : EntityEquipmentSlot.values()) {
			if (equipmentSlot.func_188452_c() == index) {
				return equipmentSlot;
			}
		}

		throw new IllegalArgumentException(String.format("Invalid equipment slot index %d", index));
	}
}
