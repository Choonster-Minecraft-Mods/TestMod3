package choonster.testmod3.world.entity;

import choonster.testmod3.init.ModEntities;
import choonster.testmod3.init.ModItems;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * An arrow entity that tells its shooter which block it hit.
 * <p>
 * Test for this thread:
 * https://forums.minecraftforge.net/topic/42456-where-can-i-find-the-phantom-block/
 *
 * @author Choonster
 */
public class BlockDetectionArrow extends ModArrow {
	public BlockDetectionArrow(final EntityType<? extends BlockDetectionArrow> entityType, final Level world) {
		super(entityType, world);
	}

	public BlockDetectionArrow(final Level world, final LivingEntity shooter) {
		super(world, shooter);
	}

	@Override
	public EntityType<?> getType() {
		return ModEntities.BLOCK_DETECTION_ARROW.get();
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(ModItems.BLOCK_DETECTION_ARROW.get());
	}

	@Override
	protected void onHit(final HitResult result) {
		super.onHit(result);

		final Entity shooter = getOwner();

		if (result.getType() == HitResult.Type.BLOCK && shooter != null) {
			final BlockPos pos = ((BlockHitResult) result).getBlockPos();
			final BlockState state = level.getBlockState(pos);

			shooter.sendMessage(new TranslatableComponent("[%s] Block at %s,%s,%s: %s", level.isClientSide ? "CLIENT" : "SERVER", pos.getX(), pos.getY(), pos.getZ(), state), Util.NIL_UUID);
		}
	}
}
