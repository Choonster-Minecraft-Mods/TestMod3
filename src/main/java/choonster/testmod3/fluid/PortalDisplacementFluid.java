package choonster.testmod3.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fluids.ForgeFlowingFluid;

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
	protected PortalDisplacementFluid(final Properties properties) {
		super(properties);
	}

	@Override
	protected boolean canDisplace(final IFluidState state, final IBlockReader world, final BlockPos pos, final Fluid fluidIn, final Direction direction) {
		final BlockState blockState = world.getBlockState(pos);

		if (blockState.getBlock() == Blocks.NETHER_PORTAL || blockState.getBlock() == Blocks.END_PORTAL || blockState.getBlock() == Blocks.END_GATEWAY) {
			return true;
		}

		return super.canDisplace(state, world, pos, fluidIn, direction);
	}

	public static class Flowing extends ForgeFlowingFluid {
		public Flowing(final Properties properties) {
			super(properties);
			setDefaultState(getStateContainer().getBaseState().with(LEVEL_1_8, 7));
		}

		@Override
		protected void fillStateContainer(final StateContainer.Builder<Fluid, IFluidState> builder) {
			super.fillStateContainer(builder);
			builder.add(LEVEL_1_8);
		}

		@Override
		public int getLevel(final IFluidState state) {
			return state.get(LEVEL_1_8);
		}

		@Override
		public boolean isSource(final IFluidState state) {
			return false;
		}
	}

	public static class Source extends ForgeFlowingFluid {
		public Source(final Properties properties) {
			super(properties);
		}

		@Override
		public int getLevel(final IFluidState state) {
			return 8;
		}

		@Override
		public boolean isSource(final IFluidState state) {
			return true;
		}
	}
}
