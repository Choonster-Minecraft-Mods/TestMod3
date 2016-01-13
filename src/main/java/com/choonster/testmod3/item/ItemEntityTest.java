package com.choonster.testmod3.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public class ItemEntityTest extends ItemTestMod3 {
	public ItemEntityTest() {
		super("entityTest");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch);
		float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw);

		double posX = player.prevPosX + (player.posX - player.prevPosX);
		double posY = player.prevPosY + (player.posY - player.prevPosY); // Entity.yOffset doesn't exist any more
		double posZ = player.prevPosZ + (player.posZ - player.prevPosZ);
		Vec3 startVector = new Vec3(posX, posY, posZ); // Use Vec3 constructor instead of Vec3.createVectorHelper

		final float degreesToRadians = (float) (Math.PI / 180);

		// I don't fully understand these values, so I haven't named them
		float f3 = MathHelper.cos(-yaw * degreesToRadians - (float) Math.PI);
		float f4 = MathHelper.sin(-yaw * degreesToRadians - (float) Math.PI);
		float f5 = -MathHelper.cos(-pitch * degreesToRadians);
		float f6 = MathHelper.sin(-pitch * degreesToRadians);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double multiplier = 5.0;
		Vec3 endVector = startVector.addVector((double) f7 * multiplier, (double) f6 * multiplier, (double) f8 * multiplier);
		MovingObjectPosition movingobjectposition = world.rayTraceBlocks(startVector, endVector, true);

		if (movingobjectposition == null) {
			return itemStack;
		} else {
			Vec3 lookVector = player.getLook(1.0F);
			List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().addCoord(lookVector.xCoord * multiplier, lookVector.yCoord * multiplier, lookVector.zCoord * multiplier).expand(1.0, 1.0, 1.0)); // Use entity.getEntityBoundingBox() instead of entity.boundingBox

			for (Entity entity : entities) {
				if (entity.canBeCollidedWith()) {
					double collisionBorderSize = entity.getCollisionBorderSize();
					AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);

					if (axisalignedbb.isVecInside(startVector)) {
						return itemStack;
					}
				}
			}


			if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				BlockPos blockPos = movingobjectposition.getBlockPos(); // Use blockPos instead of coordinate variables

				if (world.getBlockState(blockPos).getBlock() == Blocks.snow) // Use world.getBlockState().getBlock() instead of world.getBlock()
				{
					blockPos = blockPos.down(); // Use blockPos.down() instead of subtracting 1 from y coordinate
				}

				EntityPig entity = new EntityPig(world);
				entity.rotationYaw = (float) (((MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);
				entity.setPosition(blockPos.getX(), blockPos.getY() + 2, blockPos.getZ());

				if (!world.getCollidingBoundingBoxes(entity, entity.getEntityBoundingBox().expand(-0.1D, -0.1D, -0.1D)).isEmpty()) {
					return itemStack;
				}

				if (!world.isRemote) {
					world.spawnEntityInWorld(entity);
				}

				if (!player.capabilities.isCreativeMode) {
					--itemStack.stackSize;
				}
			}

			return itemStack;
		}
	}

}
