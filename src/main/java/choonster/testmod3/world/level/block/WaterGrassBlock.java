package choonster.testmod3.world.level.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Tall grass that renders with water around it while in water.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/32069-18-solved-water-and-coral-in-one-block/
 *
 * @author Choonster
 */
public class WaterGrassBlock extends BaseCoralPlantTypeBlock implements SimpleWaterloggedBlock {
	private static final VoxelShape SHAPE = Util.make(() -> {
		final float size = 6.4f;
		return box(8 - size, 0, 8 - size, 8 + size, 12.8, 8 + size);
	});

	public WaterGrassBlock(final BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(final BlockState state, final BlockGetter world, final BlockPos pos, final CollisionContext context) {
		return SHAPE;
	}
}
