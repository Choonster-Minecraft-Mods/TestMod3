package choonster.testmod3.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

/**
 * A fluid that doesn't flow horizontally.
 *
 * @author Choonster
 */
public abstract class StaticFluid extends ForgeFlowingFluid {
	protected StaticFluid(final Properties properties) {
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
		return direction.getAxis() == Direction.Axis.Y && super.canMaybePassThrough(
				blockGetter,
				pos,
				blockState,
				direction,
				targetPos,
				targetBlockState,
				targetFluidState
		);
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
