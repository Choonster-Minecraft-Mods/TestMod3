package com.choonster.testmod3.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IThrowableEntity;

/**
 * An arrow entity that behaves like the vanilla arrow but renders with a different texture.
 *
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2577561-custom-arrow
 */
public class EntityModArrow extends EntityArrow implements IThrowableEntity {
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
}
