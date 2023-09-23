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
// TODO: Properly implement portal displacement
public abstract class PortalDisplacementFluid extends ForgeFlowingFluid {
	private static final Method CAN_PASS_THROUGH_WALL = ObfuscationReflectionHelper.findMethod(FlowingFluid.class, /* canPassThroughWall */ "m_76061_", Direction.class, BlockGetter.class, BlockPos.class, BlockState.class, BlockPos.class, BlockState.class);

	protected PortalDisplacementFluid(final Properties properties) {
		super(properties);
	}

	// TODO: Override getSlopeDistance and getSpread to call modified version of canPassThrough

	@Override
	protected boolean canSpreadTo(final BlockGetter level, final BlockPos fromPos, final BlockState fromBlockState, final Direction direction, final BlockPos toPos, final BlockState toBlockState, final FluidState toFluidState, final Fluid fluidIn) {
		try {
			return toFluidState.canBeReplacedWith(level, toPos, fluidIn, direction) &&
					(boolean) CAN_PASS_THROUGH_WALL.invoke(this, direction, level, fromPos, fromBlockState, toPos, toBlockState) &&
					canHoldFluid(level, toPos, toBlockState, fluidIn);
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to invoke FlowingFluid.canPassThroughWall", e);
		}
	}

	@Override
	protected boolean canBeReplacedWith(final FluidState state, final BlockGetter world, final BlockPos pos, final Fluid fluidIn, final Direction direction) {
		final var blockState = world.getBlockState(pos);

		if (blockState.getBlock() == Blocks.NETHER_PORTAL || blockState.getBlock() == Blocks.END_PORTAL || blockState.getBlock() == Blocks.END_GATEWAY) {
			return true;
		}

		return super.canBeReplacedWith(state, world, pos, fluidIn, direction);
	}

	@SuppressWarnings("deprecation")
	private static boolean canHoldFluid(final BlockGetter world, final BlockPos pos, final BlockState state, final Fluid fluid) {
		final var block = state.getBlock();

		if (block instanceof final LiquidBlockContainer liquidBlockContainer) {
			return liquidBlockContainer.canPlaceLiquid(null, world, pos, state, fluid);
		}

		if (!(block instanceof DoorBlock) && !state.is(BlockTags.SIGNS) && !state.is(Blocks.LADDER) && !state.is(Blocks.SUGAR_CANE) && !state.is(Blocks.BUBBLE_COLUMN)) {
			if (!state.is(Blocks.STRUCTURE_VOID)) {
				return !state.blocksMotion();
			}

			return false;
		}

		return false;
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
