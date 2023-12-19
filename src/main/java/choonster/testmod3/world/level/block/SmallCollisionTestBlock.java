package choonster.testmod3.world.level.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A block with a bounding box slightly smaller than a full cube so entities can collide with it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,38530.0.html
 *
 * @author Choonster
 */
public class SmallCollisionTestBlock extends Block {
	/**
	 * The block's collision shape.
	 */
	private static final VoxelShape COLLISION_SHAPE = Util.make(() -> {
		final double offset = 0.0176;
		final double min = 0 + offset;
		final double max = 1 - offset;
		return box(min, min, min, max, max, max);
	});

	public static final MapCodec<SmallCollisionTestBlock> CODEC = simpleCodec(SmallCollisionTestBlock::new);

	public SmallCollisionTestBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext selectionContext) {
		return COLLISION_SHAPE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(final BlockState state, final Level level, final BlockPos pos, final Entity entity) {
		if (entity instanceof LivingEntity) {
			((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 10, 0));
		}
	}
}
