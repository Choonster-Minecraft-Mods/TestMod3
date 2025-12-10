package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

/**
 * Stores the keys for this mod's {@link net.minecraft.world.item.equipment.EquipmentAsset EquipmentAssets}.
 *
 * @author Choonster
 */
public class ModEquipmentAssets {
	public static ResourceKey<EquipmentAsset> REPLACEMENT = createId("replacement");

	private static ResourceKey<EquipmentAsset> createId(final String name) {
		return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(TestMod3.MODID, name));
	}
}
