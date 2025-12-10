package choonster.testmod3.world.entity;

import choonster.testmod3.init.ModEntities;
import choonster.testmod3.init.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

/**
 * An arrow entity that tells its shooter which block it hit.
 * <p>
 * Test for this thread:
 * https://forums.minecraftforge.net/topic/42456-where-can-i-find-the-phantom-block/
 *
 * @author Choonster
 */
public class BlockDetectionArrow extends ModArrow {
	public BlockDetectionArrow(final EntityType<? extends ModArrow> entityType, final Level level) {
		super(entityType, level);
	}

	public BlockDetectionArrow(final Level level, final LivingEntity shooter, final ItemStack pickupItemStack, @Nullable final ItemStack firedFromWeapon) {
		super(level, shooter, pickupItemStack, firedFromWeapon);
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

		final var shooter = getOwner();

		if (result.getType() == HitResult.Type.BLOCK && shooter instanceof final Player player) {
			final var pos = ((BlockHitResult) result).getBlockPos();
			final var state = level().getBlockState(pos);

			player.displayClientMessage(
					Component.translatable(
							"[%s] Block at %s,%s,%s: %s",
							level().isClientSide() ? "CLIENT" : "SERVER",
							pos.getX(),
							pos.getY(),
							pos.getZ(),
							state
					),
					false
			);
		}
	}
}
