package choonster.testmod3.item;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.RegistryUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for this mod's armour items
 *
 * @author Choonster
 */
public class ItemArmourTestMod3 extends ItemArmor {
	private static final Map<EntityEquipmentSlot, String> SLOT_NAMES;

	static {
		final Map<EntityEquipmentSlot, String> slotNames = new HashMap<>();
		slotNames.put(EntityEquipmentSlot.HEAD, "helmet");
		slotNames.put(EntityEquipmentSlot.CHEST, "chestplate");
		slotNames.put(EntityEquipmentSlot.LEGS, "leggings");
		slotNames.put(EntityEquipmentSlot.FEET, "boots");

		SLOT_NAMES = Maps.immutableEnumMap(slotNames);
	}


	public ItemArmourTestMod3(final ArmorMaterial material, final EntityEquipmentSlot equipmentSlot, final String armourName) {
		super(material, -1, equipmentSlot);

		Preconditions.checkArgument(SLOT_NAMES.containsKey(equipmentSlot), "Invalid slot %s", equipmentSlot);

		RegistryUtil.setItemName(this, armourName + "_" + SLOT_NAMES.get(equipmentSlot));
		setCreativeTab(TestMod3.creativeTab);
	}
}
