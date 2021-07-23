package choonster.testmod3.fluid;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;

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
	protected boolean canSpreadTo(final BlockGetter level, final BlockPos fromPos, final BlockState fromBlockState, final Direction direction, final BlockPos toPos, final BlockState toBlockState, final FluidState toFluidState, final Fluid fluidIn) {
		return direction.getAxis() == Direction.Axis.Y && super.canSpreadTo(level, fromPos, fromBlockState, direction, toPos, toBlockState, toFluidState, fluidIn);
	}

	public static class Flowing extends StaticFluid {
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

	public static class Source extends StaticFluid {
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
