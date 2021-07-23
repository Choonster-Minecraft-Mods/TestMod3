package choonster.testmod3.util;

import choonster.testmod3.TestMod3;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constants {
	public static final String RESOURCE_PREFIX = TestMod3.MODID + ":";

	/**
	 * The armour equipment slots.
	 */
	public static final Set<EquipmentSlot> ARMOUR_SLOTS = ImmutableSet.copyOf(
			Stream.of(EquipmentSlot.values())
					.filter(equipmentSlot -> equipmentSlot.getType() == EquipmentSlot.Type.ARMOR)
					.collect(Collectors.toList())
	);
}
