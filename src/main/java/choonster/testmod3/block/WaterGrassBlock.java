package choonster.testmod3.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

/**
 * Tall grass that renders with water around it while in water.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/32069-18-solved-water-and-coral-in-one-block/
 *
 * @author Choonster
 */
public class WaterGrassBlock extends BushBlock implements ILiquidContainer {
	private static final VoxelShape SHAPE = Util.make(() -> {
		final float size = 6.4f;
		return makeCuboidShape(8 - size, 0, 8 - size, 8 + size, 12.8, 8 + size);
	});

	public WaterGrassBlock(final Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(final BlockState state, final IBlockReader world, final BlockPos pos, final ISelectionContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(final BlockItemUseContext context) {
		final FluidState fluidState = context.getWorld().getFluidState(context.getPos());

		return fluidState.isTagged(FluidTags.WATER) && fluidState.getLevel() == 8 ? super.getStateForPlacement(context) : null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(final BlockState state, final Direction facing, final BlockState facingState, final IWorld world, final BlockPos currentPos, final BlockPos facingPos) {
		final BlockState newState = super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);

		if (!newState.isAir(world, currentPos)) {
			world.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return newState;
	}

	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(final BlockState state) {
		return Fluids.WATER.getStillFluidState(false);
	}

	@Override
	public boolean canContainFluid(final IBlockReader world, final BlockPos pos, final BlockState state, final Fluid fluid) {
		return false;
	}

	@Override
	public boolean receiveFluid(final IWorld world, final BlockPos pos, final BlockState state, final FluidState fluidState) {
		return false;
	}
}
