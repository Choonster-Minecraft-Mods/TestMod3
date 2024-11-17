package choonster.testmod3.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A fluid that displaces portals.
 * <p>
 * Test for this thread:
 * https://www.minecraftforge.net/forum/topic/41373-1102-1-more-fluid-problem/
 *
 * @author Choonster
 */
public abstract class PortalDisplacementFluid extends ForgeFlowingFluid {
	private static final Method CAN_PASS_THROUGH_WALL = ObfuscationReflectionHelper.findMethod(
			FlowingFluid.class,
			"canPassThroughWall",
			Direction.class,
			BlockGetter.class,
			BlockPos.class,
			BlockState.class,
			BlockPos.class,
			BlockState.class
	);

	private static final Method CAN_HOLD_SPECIFIC_FLUID = ObfuscationReflectionHelper.findMethod(
			FlowingFluid.class,
			"canHoldSpecificFluid",
			BlockGetter.class,
			BlockPos.class,
			BlockState.class,
			Fluid.class
	);

	protected PortalDisplacementFluid(final Properties properties) {
		super(properties);
	}

	// This has been made protected with an access transformer
	@Override
	protected boolean canMaybePassThrough(
			final BlockGetter blockGetter,
			final BlockPos pos,
			final BlockState blockState,
			final Direction direction,
			final BlockPos targetPos,
			final BlockState targetBlockState,
			final FluidState targetFluidState
	) {
		return !isSourceBlockOfThisType(targetFluidState)
				&& canHoldAnyFluidAllowPortals(targetBlockState)
				&& canPassThroughWall(direction, blockGetter, pos, blockState, targetPos, targetBlockState);
	}

	private static boolean canPassThroughWall(
			final Direction direction,
			final BlockGetter blockGetter,
			final BlockPos pos,
			final BlockState blockState,
			final BlockPos targetPos,
			final BlockState targetBlockState
	) {
		try {
			return (boolean) CAN_PASS_THROUGH_WALL.invoke(
					null,
					direction,
					blockGetter,
					pos,
					blockState,
					targetPos,
					targetBlockState
			);
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to invoke FlowingFluid.canPassThroughWall", e);
		}
	}

	private boolean isSourceBlockOfThisType(final FluidState fluidState) {
		return fluidState.getType().isSame(this) && fluidState.isSource();
	}

	@Override
	protected boolean isWaterHole(
			final BlockGetter blockGetter,
			final BlockPos pos,
			final BlockState blockState,
			final BlockPos targetPos,
			final BlockState targetBlockState
	) {
		if (!canPassThroughWall(Direction.DOWN, blockGetter, pos, blockState, targetPos, targetBlockState)) {
			return false;
		}

		return targetBlockState.getFluidState().getType().isSame(this)
				|| canHoldFluidAllowPortals(blockGetter, targetPos, targetBlockState, getFlowing());
	}

	@SuppressWarnings("deprecation")
	private static boolean canHoldAnyFluidAllowPortals(final BlockState state) {
		final var block = state.getBlock();
		if (block instanceof LiquidBlockContainer) {
			return true;
		}

		return !state.blocksMotion()
				&& !(block instanceof DoorBlock)
				&& !state.is(BlockTags.SIGNS)
				&& !state.is(Blocks.LADDER)
				&& !state.is(Blocks.SUGAR_CANE)
				&& !state.is(Blocks.BUBBLE_COLUMN)
				&& !state.is(Blocks.STRUCTURE_VOID);
	}

	private static boolean canHoldFluidAllowPortals(
			final BlockGetter blockGetter,
			final BlockPos pos,
			final BlockState state,
			final Fluid fluid
	) {
		try {
			return canHoldAnyFluidAllowPortals(state)
					&& (boolean) CAN_HOLD_SPECIFIC_FLUID.invoke(null, blockGetter, pos, state, fluid);
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to call canHoldSpecificFluid", e);
		}
	}

	public static class Flowing extends PortalDisplacementFluid {
		public Flowing(final Properties properties) {
			super(properties);
			registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
		}

		@Override
		protected void createFluidStateDefinition(final StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getAmount(final FluidState state) {
			return state.getValue(LEVEL);
		}

		@Override
		public boolean isSource(final FluidState state) {
			return false;
		}
	}

	public static class Source extends PortalDisplacementFluid {
		public Source(final Properties properties) {
			super(properties);
		}

		@Override
		public int getAmount(final FluidState state) {
			return 8;
		}

		@Override
		public boolean isSource(final FluidState state) {
			return true;
		}
	}
}
