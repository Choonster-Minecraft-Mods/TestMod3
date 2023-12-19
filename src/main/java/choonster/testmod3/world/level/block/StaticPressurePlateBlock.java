package choonster.testmod3.world.level.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A block that uses pressure plate model, placement and piston movement behaviour; but doesn't depress when stood on.
 *
 * @author Choonster
 */
public abstract class StaticPressurePlateBlock extends Block {
	protected final VoxelShape SHAPE = box(1, 0, 1, 15, 0.5, 15.0);

	public StaticPressurePlateBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	protected abstract MapCodec<? extends Block> codec();

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(final BlockState state, final BlockGetter world, final BlockPos pos, final CollisionContext context) {
		return SHAPE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(final BlockState state, final Direction facing, final BlockState facingState, final LevelAccessor world, final BlockPos currentPos, final BlockPos facingPos) {
		return facing == Direction.DOWN && !state.canSurvive(world, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, world, currentPos, facingPos);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canSurvive(final BlockState state, final LevelReader world, final BlockPos pos) {
		final BlockPos downPos = pos.below();
		return canSupportRigidBlock(world, downPos) || canSupportCenter(world, downPos, Direction.UP);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(final BlockState state, final BlockGetter world, final BlockPos pos, final CollisionContext context) {
		return Shapes.empty();
	}
}
