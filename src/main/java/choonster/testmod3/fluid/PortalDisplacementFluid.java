package choonster.testmod3.fluid;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

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
	private static final Method DOES_SIDE_HAVE_HOLES = ObfuscationReflectionHelper.findMethod(FlowingFluid.class, /* doesSideHaveHoles */ "func_212751_a", Direction.class, IBlockReader.class, BlockPos.class, BlockState.class, BlockPos.class, BlockState.class);

	protected PortalDisplacementFluid(final Properties properties) {
		super(properties);
	}

	@Override
	protected boolean canFlow(final IBlockReader worldIn, final BlockPos fromPos, final BlockState fromBlockState, final Direction direction, final BlockPos toPos, final BlockState toBlockState, final FluidState toFluidState, final Fluid fluidIn) {
		try {
			return toFluidState.canDisplace(worldIn, toPos, fluidIn, direction) &&
					(boolean) DOES_SIDE_HAVE_HOLES.invoke(this, direction, worldIn, fromPos, fromBlockState, toPos, toBlockState) &&
					isNotBlocked(worldIn, toPos, toBlockState, fluidIn);
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to invoke FlowingFluid.doesSideHaveHoles", e);
		}
	}

	@Override
	protected boolean canDisplace(final FluidState state, final IBlockReader world, final BlockPos pos, final Fluid fluidIn, final Direction direction) {
		final BlockState blockState = world.getBlockState(pos);

		if (blockState.getBlock() == Blocks.NETHER_PORTAL || blockState.getBlock() == Blocks.END_PORTAL || blockState.getBlock() == Blocks.END_GATEWAY) {
			return true;
		}

		return super.canDisplace(state, world, pos, fluidIn, direction);
	}

	private boolean isNotBlocked(final IBlockReader world, final BlockPos pos, final BlockState state, final Fluid fluid) {
		final Block block = state.getBlock();

		if (block instanceof ILiquidContainer) {
			return ((ILiquidContainer) block).canContainFluid(world, pos, state, fluid);
		}

		if (!(block instanceof DoorBlock) && !block.isIn(BlockTags.SIGNS) && block != Blocks.LADDER && block != Blocks.SUGAR_CANE && block != Blocks.BUBBLE_COLUMN) {
			final Material material = state.getMaterial();

			if (material != Material.STRUCTURE_VOID && material != Material.OCEAN_PLANT && material != Material.SEA_GRASS) {
				return !material.blocksMovement();
			}

			return false;
		}

		return false;
	}

	public static class Flowing extends PortalDisplacementFluid {
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

	public static class Source extends PortalDisplacementFluid {
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
