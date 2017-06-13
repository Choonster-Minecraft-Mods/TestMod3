package choonster.testmod3.block.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;

import java.util.Random;

/**
 * A fluid block that doesn't flow.
 *
 * @author Choonster
 */
public class BlockFluidNoFlow extends BlockFluidFinite {
	public BlockFluidNoFlow(final Fluid fluid, final Material material) {
		super(fluid, material);
		setDefaultState(blockState.getBaseState().withProperty(LEVEL, 7));
	}

	// Adapted from BlockFluidFinite#updateTick. Only flows vertically, not horizontally.
	@Override
	public void updateTick(final World world, final BlockPos pos, final IBlockState state, final Random rand) {
		boolean changed = false;
		int quantaRemaining = state.getValue(LEVEL) + 1;

		// Flow vertically if possible
		final int prevRemaining = quantaRemaining;
		quantaRemaining = tryToFlowVerticallyInto(world, pos, quantaRemaining);

		if (quantaRemaining < 1) {
			return;
		} else if (quantaRemaining != prevRemaining) {
			changed = true;
			if (quantaRemaining == 1) {
				world.setBlockState(pos, state.withProperty(LEVEL, quantaRemaining - 1), 2);
				return;
			}
		} else if (quantaRemaining == 1) {
			return;
		}

		if (changed) {
			world.setBlockState(pos, state.withProperty(LEVEL, quantaRemaining - 1), 2);
		}
	}
}
