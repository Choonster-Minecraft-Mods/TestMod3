package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import java.util.Collections;

/**
 * A tool that can function as a sword, pickaxe, axe or shovel.
 * <p>
 * Currently mines some blocks (e.g. Block of Coal, Dropper, Stairs, Doors) slower than the proper tool should while mining others at the proper speed.
 * I don't know why this is.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2550421-how-to-make-a-tool-e-g-a-sword-have-the-abilities
 */
public class ItemHarvestSword extends ItemTool {
	public ItemHarvestSword(ToolMaterial toolMaterial) {
		super(4.0f, toolMaterial, Collections.EMPTY_SET);
		setHarvestLevel("pickaxe", toolMaterial.getHarvestLevel());
		setHarvestLevel("axe", toolMaterial.getHarvestLevel());
		setHarvestLevel("shovel", toolMaterial.getHarvestLevel());
		setCreativeTab(TestMod3.creativeTab);
	}

	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker) {
		itemStack.damageItem(1, attacker); // Only reduce the durability by 1 point (like swords do) instead of 2 (like tools do)
		return true;
	}
}
