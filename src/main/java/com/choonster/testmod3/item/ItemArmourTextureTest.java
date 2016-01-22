package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * An armour item that uses the armour textures specified in its constructor instead of those of its {@link ItemArmor.ArmorMaterial}.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2600608-armor-model-texture-problem
 *
 * @author Choonster
 */
public class ItemArmourTextureTest extends ItemArmor {
	/**
	 * {@link #armorType} to slot name mapping
	 */
	private static final List<String> SLOT_NAMES = ImmutableList.of("helmet", "chestplate", "leggings", "boots");

	/**
	 * The path of the armour texture. Used as the format string in {@link String#format(String, Object...)} with the slot and type as arguments.
	 */
	private final String texturePath;

	public ItemArmourTextureTest(ArmorMaterial material, int armorType, String texturePath, String armourName) {
		super(material, -1, armorType);
		this.texturePath = texturePath;
		setUnlocalizedName("testmod3." + SLOT_NAMES.get(armorType) + armourName);
		setCreativeTab(TestMod3.creativeTab);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return String.format(texturePath, slot == 2 ? 2 : 1, type != null ? type : "");
	}
}
