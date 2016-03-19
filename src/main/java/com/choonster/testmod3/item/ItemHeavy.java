package com.choonster.testmod3.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * An item that applies Slowness II to the player while it's in their inventory.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2423521-detecting-item-in-inventory
 *
 * @author Choonster
 */
public class ItemHeavy extends ItemTestMod3 {
	public ItemHeavy() {
		super("heavy");
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		if (entityIn instanceof EntityLivingBase) { // If the Entity is an instance of EntityLivingBase,
			((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.moveSlowdown, 10, 1)); // Apply Slowness II (amplifier = 1) for 10 ticks (0.5 seconds)
		}
	}
}
