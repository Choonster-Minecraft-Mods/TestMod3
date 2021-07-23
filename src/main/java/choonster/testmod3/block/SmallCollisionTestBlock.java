package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

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

	public SmallCollisionTestBlock(final Block.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(final BlockState state, final IBlockReader world, final BlockPos pos, final ISelectionContext selectionContext) {
		return COLLISION_SHAPE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(final BlockState state, final World world, final BlockPos pos, final Entity entity) {
		if (entity instanceof LivingEntity) {
			((LivingEntity) entity).addEffect(new EffectInstance(Effects.ABSORPTION, 10, 0));
		}
	}
}
