package choonster.testmod3.world.level.block;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;

/**
 * A block that writes a message to the log when an item collides with it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34022.0.html
 *
 * @author Choonster
 */
public class ItemCollisionTestBlock extends Block {
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final MapCodec<ItemCollisionTestBlock> CODEC = simpleCodec(ItemCollisionTestBlock::new);

	private static final VoxelShape SHAPE = Util.make(() -> {
		// A small value to offset each side of the block's bounding box by to allow entities to collide with the block
		// and thus call onEntityCollidedWithBlock
		final float minBound = 0.16f;
		final float maxBound = 16 - minBound;

		return box(minBound, minBound, minBound, maxBound, maxBound, maxBound);
	});

	public ItemCollisionTestBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@Override
	protected void entityInside(
			final BlockState state,
			final Level level,
			final BlockPos pos,
			final Entity entity,
			final InsideBlockEffectApplier insideBlockEffectApplier,
			final boolean _unknown
	) {
		super.entityInside(state, level, pos, entity, insideBlockEffectApplier, _unknown);

		if (!level.isClientSide() && entity instanceof ItemEntity) {
			LOGGER.info("Collision at {}: {}", pos, entity);
		}
	}

	@Override
	public VoxelShape getShape(final BlockState state, final BlockGetter world, final BlockPos pos, final CollisionContext context) {
		return SHAPE;
	}
}
