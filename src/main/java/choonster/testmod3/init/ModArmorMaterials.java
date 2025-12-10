package choonster.testmod3.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.EnumMap;

/**
 * Registers this mod's {@link ArmorMaterial ArmorMaterials}.
 *
 * @author Choonster
 */
public class ModArmorMaterials {
	public static final ArmorMaterial REPLACEMENT = new ArmorMaterial(
			15,
			Util.make(new EnumMap<>(ArmorType.class), map -> {
				map.put(ArmorType.BOOTS, 1);
				map.put(ArmorType.LEGGINGS, 4);
				map.put(ArmorType.CHESTPLATE, 5);
				map.put(ArmorType.HELMET, 2);
			}),
			12,
			SoundEvents.ARMOR_EQUIP_CHAIN,
			0,
			0,
			ModTags.Items.REPAIRS_REPLACEMENT_ARMOR,
			ModEquipmentAssets.REPLACEMENT
	);
}
