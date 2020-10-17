package choonster.testmod3.entity;

import choonster.testmod3.init.ModEntities;
import choonster.testmod3.init.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * An arrow entity that tells its shooter which block it hit.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php?topic=42677.0
 *
 * @author Choonster
 */
public class BlockDetectionArrowEntity extends ModArrowEntity {
	public BlockDetectionArrowEntity(final EntityType<? extends BlockDetectionArrowEntity> entityType, final World world) {
		super(entityType, world);
	}

	public BlockDetectionArrowEntity(final World world, final LivingEntity shooter) {
		super(world, shooter);
	}

	@Override
	public EntityType<?> getType() {
		return ModEntities.BLOCK_DETECTION_ARROW;
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(ModItems.BLOCK_DETECTION_ARROW);
	}

	@Override
	protected void onImpact(final RayTraceResult result) {
		super.onImpact(result);

		final Entity shootingEntity = /* getShooter */ func_234616_v_();

		if (result.getType() == RayTraceResult.Type.BLOCK && shootingEntity != null) {
			final BlockPos pos = ((BlockRayTraceResult) result).getPos();
			final BlockState state = world.getBlockState(pos);

			shootingEntity.sendMessage(new TranslationTextComponent("[%s] Block at %s,%s,%s: %s", world.isRemote ? "CLIENT" : "SERVER", pos.getX(), pos.getY(), pos.getZ(), state), Util.DUMMY_UUID);
		}
	}
}
