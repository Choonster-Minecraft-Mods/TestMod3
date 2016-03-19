package com.choonster.testmod3.item;

import com.choonster.testmod3.entity.EntityModArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * An arrow fired from this mod's bows.
 *
 * @author Choonster
 */
public class ItemModArrow extends ItemArrow {
	public ItemModArrow(String itemName) {
		ItemTestMod3.setItemName(this, itemName);
	}

	@Override
	public EntityArrow makeTippedArrow(World worldIn, ItemStack p_185052_2_, EntityLivingBase shooter) {
		EntityModArrow entityModArrow = new EntityModArrow(worldIn, shooter);
		entityModArrow.setPotionEffect(p_185052_2_);
		return entityModArrow;
	}
}
