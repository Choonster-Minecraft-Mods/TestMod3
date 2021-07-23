package choonster.testmod3.world.level.block.entity;

import choonster.testmod3.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * A BlockEntity that applies a potion effect to all entities within a certain distance of it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/35606-189-applying-a-potion-effect-via-a-te/
 *
 * @author Choonster
 */
public class PotionEffectBlockEntity extends BlockEntity {
	private static final int RADIUS = 2;

	public PotionEffectBlockEntity(final BlockPos pos, final BlockState state) {
		super(ModBlockEntities.POTION_EFFECT.get(), pos, state);
	}

	public static void tick(final Level level, final BlockPos pos, final BlockState state, final PotionEffectBlockEntity blockEntity) {
		if (!level.isClientSide) {
			final AABB areaToSearch = new AABB(pos.offset(-RADIUS, -RADIUS, -RADIUS), pos.offset(RADIUS, RADIUS, RADIUS));
			final List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, areaToSearch);

			entities.stream()
					.filter(entity -> !entity.hasEffect(MobEffects.POISON))
					.forEach(entity -> entity.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 1)));
		}
	}
}
