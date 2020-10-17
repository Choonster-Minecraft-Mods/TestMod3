package choonster.testmod3.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fluids.ForgeFlowingFluid;

/**
 * A fluid that doesn't flow horizontally.
 *
 * @author Choonster
 */
// TODO: Properly implement flowing
public abstract class StaticFluid extends ForgeFlowingFluid {
	protected StaticFluid(final Properties properties) {
		super(properties);
	}

	@Override
	protected boolean canFlow(final IBlockReader worldIn, final BlockPos fromPos, final BlockState fromBlockState, final Direction direction, final BlockPos toPos, final BlockState toBlockState, final FluidState toFluidState, final Fluid fluidIn) {
		return direction.getAxis() == Direction.Axis.Y && super.canFlow(worldIn, fromPos, fromBlockState, direction, toPos, toBlockState, toFluidState, fluidIn);
	}

	public static class Flowing extends StaticFluid {
		public Flowing(final Properties properties) {
			super(properties);
			setDefaultState(getStateContainer().getBaseState().with(LEVEL_1_8, 7));
		}

		@Override
		protected void fillStateContainer(final StateContainer.Builder<Fluid, FluidState> builder) {
			super.fillStateContainer(builder);
			builder.add(LEVEL_1_8);
		}

		@Override
		public int getLevel(final FluidState state) {
			return state.get(LEVEL_1_8);
		}

		@Override
		public boolean isSource(final FluidState state) {
			return false;
		}
	}

	public static class Source extends StaticFluid {
		public Source(final Properties properties) {
			super(properties);
		}

		@Override
		public int getLevel(final FluidState state) {
			return 8;
		}

		@Override
		public boolean isSource(final FluidState state) {
			return true;
		}
	}
}
