package choonster.testmod3.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.List;

/**
 * An item that places a Pig where the player is looking when used. Based on {@link ItemBoat}.
 *
 * @author Choonster
 */
public class ItemEntityTest extends ItemTestMod3 {
	public ItemEntityTest() {
		super("entity_test");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
		final float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch);
		final float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw);

		final double posX = player.prevPosX + (player.posX - player.prevPosX);
		final double posY = player.prevPosY + (player.posY - player.prevPosY); // Entity.yOffset doesn't exist any more
		final double posZ = player.prevPosZ + (player.posZ - player.prevPosZ);
		final Vec3d startVector = new Vec3d(posX, posY, posZ); // Use Vec3 constructor instead of Vec3.createVectorHelper

		final float degreesToRadians = (float) (Math.PI / 180);

		// I don't fully understand these values, so I haven't named them
		final float f3 = MathHelper.cos(-yaw * degreesToRadians - (float) Math.PI);
		final float f4 = MathHelper.sin(-yaw * degreesToRadians - (float) Math.PI);
		final float f5 = -MathHelper.cos(-pitch * degreesToRadians);
		final float f6 = MathHelper.sin(-pitch * degreesToRadians);
		final float f7 = f4 * f5;
		final float f8 = f3 * f5;
		final double multiplier = 5.0;
		final Vec3d endVector = startVector.addVector((double) f7 * multiplier, (double) f6 * multiplier, (double) f8 * multiplier);
		final RayTraceResult rayTraceResult = world.rayTraceBlocks(startVector, endVector, true);

		if (rayTraceResult == null) return new ActionResult<>(EnumActionResult.PASS, itemStack);

		final Vec3d lookVector = player.getLook(1.0F);
		final List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().addCoord(lookVector.xCoord * multiplier, lookVector.yCoord * multiplier, lookVector.zCoord * multiplier).expand(1.0, 1.0, 1.0)); // Use entity.getEntityBoundingBox() instead of entity.boundingBox

		for (final Entity entity : entities) {
			if (entity.canBeCollidedWith()) {
				final double collisionBorderSize = entity.getCollisionBorderSize();
				final AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);

				if (axisAlignedBB.isVecInside(startVector)) {
					return new ActionResult<>(EnumActionResult.PASS, itemStack);
				}
			}
		}

		if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos blockPos = rayTraceResult.getBlockPos(); // Use blockPos instead of coordinate variables

			if (world.getBlockState(blockPos).getBlock() == Blocks.SNOW) // Use world.getBlockState().getBlock() instead of world.getBlock()
			{
				blockPos = blockPos.down(); // Use blockPos.down() instead of subtracting 1 from y coordinate
			}

			final EntityPig entity = new EntityPig(world);
			entity.rotationYaw = (float) (((MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);
			entity.setPosition(blockPos.getX(), blockPos.getY() + 2, blockPos.getZ());

			if (!world.getCollisionBoxes(entity, entity.getEntityBoundingBox().expand(-0.1D, -0.1D, -0.1D)).isEmpty()) {
				return new ActionResult<>(EnumActionResult.FAIL, itemStack);
			}

			if (!world.isRemote) {
				world.spawnEntityInWorld(entity);
			}

			if (!player.capabilities.isCreativeMode) {
				--itemStack.stackSize;
			}

			player.addStat(StatList.getObjectUseStats(this));
		}

		return new ActionResult<>(EnumActionResult.PASS, itemStack);
	}
}
