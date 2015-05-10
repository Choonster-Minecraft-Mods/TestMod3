package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2423521-detecting-item-in-inventory
public class ItemHeavy extends Item {
	public ItemHeavy() {
		setCreativeTab(TestMod3.creativeTab);
		setUnlocalizedName("heavy");
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		if (entityIn instanceof EntityLivingBase) { // If the Entity is an instance of EntityLivingBase,
			((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 10, 1)); // Apply Slowness 2 (amplifier = 1) for 10 ticks (0.5 seconds)
		}
	}
}
