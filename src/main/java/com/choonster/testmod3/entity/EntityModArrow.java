package com.choonster.testmod3.entity;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IThrowableEntity;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

/**
 * An arrow entity that behaves like the vanilla arrow but renders with a different texture.
 *
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2577561-custom-arrow
 */
public class EntityModArrow extends EntityArrow implements IThrowableEntity {
	private static final Field IN_GROUND_FIELD = ReflectionHelper.findField(EntityArrow.class, "inGround", "field_70254_i");

	public EntityModArrow(World worldIn) {
		super(worldIn);
	}

	public EntityModArrow(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityModArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase p_i1755_3_, float velocity, float inaccuracy) {
		super(worldIn, shooter, p_i1755_3_, velocity, inaccuracy);
	}

	public EntityModArrow(World worldIn, EntityLivingBase shooter, float velocity) {
		super(worldIn, shooter, velocity);
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

	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn) {
		if (!this.worldObj.isRemote && isInGround() && this.arrowShake <= 0) {
			boolean canPickUp = this.canBePickedUp == 1 || this.canBePickedUp == 2 && entityIn.capabilities.isCreativeMode;

			if (this.canBePickedUp == 1 && !entityIn.inventory.addItemStackToInventory(new ItemStack(ModItems.modArrow))) {
				canPickUp = false;
			}

			if (canPickUp) {
				this.playSound("random.pop", 0.2f, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
				entityIn.onItemPickup(this, 1);
				this.setDead();
			}
		}
	}

	public boolean isInGround() {
		try {
			return (boolean) IN_GROUND_FIELD.get(this);
		} catch (IllegalAccessException e) {
			Logger.error(e, "Error checking EntityArrow#inGround");
		}

		return false;
	}
}
