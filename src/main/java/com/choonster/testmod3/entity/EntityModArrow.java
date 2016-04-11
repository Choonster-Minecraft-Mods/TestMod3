package com.choonster.testmod3.entity;

import com.choonster.testmod3.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IThrowableEntity;

/**
 * An arrow entity that behaves like the vanilla arrow but renders with a different texture.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2577561-custom-arrow
 *
 * @author Choonster
 */
public class EntityModArrow extends EntityTippedArrow implements IThrowableEntity {
	public EntityModArrow(World worldIn) {
		super(worldIn);
	}

	public EntityModArrow(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityModArrow(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	@Override
	public void setPotionEffect(ItemStack stack) {
		super.setPotionEffect(new ItemStack(Items.ARROW)); // Mod arrows can't have potion effects
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(ModItems.modArrow);
	}

	/**
	 * Gets the entity that threw/created this entity.
	 *
	 * @return The owner instance, Null if none.
	 */
	@Override
	public Entity getThrower() {
		return shootingEntity;
	}

	/**
	 * Sets the entity that threw/created this entity.
	 *
	 * @param entity The new thrower/creator.
	 */
	@Override
	public void setThrower(Entity entity) {
		shootingEntity = entity;
	}
}
