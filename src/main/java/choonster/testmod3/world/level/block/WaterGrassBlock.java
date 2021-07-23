package choonster.testmod3.world.level.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * Tall grass that renders with water around it while in water.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/32069-18-solved-water-and-coral-in-one-block/
 *
 * @author Choonster
 */
public class WaterGrassBlock extends BushBlock implements LiquidBlockContainer {
	private static final VoxelShape SHAPE = Util.make(() -> {
		final float size = 6.4f;
		return box(8 - size, 0, 8 - size, 8 + size, 12.8, 8 + size);
	});

	public WaterGrassBlock(final Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(final BlockState state, final BlockGetter world, final BlockPos pos, final CollisionContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		final FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());

		return fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8 ? super.getStateForPlacement(context) : null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(final BlockState state, final Direction facing, final BlockState facingState, final LevelAccessor world, final BlockPos currentPos, final BlockPos facingPos) {
		final BlockState newState = super.updateShape(state, facing, facingState, world, currentPos, facingPos);

		if (!newState.isAir()) {
			world.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}

		return newState;
	}

	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(final BlockState state) {
		return Fluids.WATER.getSource(false);
	}

	@Override
	public boolean canPlaceLiquid(final BlockGetter world, final BlockPos pos, final BlockState state, final Fluid fluid) {
		return false;
	}

	@Override
	public boolean placeLiquid(final LevelAccessor world, final BlockPos pos, final BlockState state, final FluidState fluidState) {
		return false;
	}
}
