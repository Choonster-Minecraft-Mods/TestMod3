package com.choonster.testmod3.block.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;

import java.util.Random;

public class BlockFluidNoFlow extends BlockFluidFinite {
	public BlockFluidNoFlow(Fluid fluid, Material material) {
		super(fluid, material);
	}

	// Adapted from BlockFluidFinite#updateTick. Only flows vertically, not horizontally.
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		boolean changed = false;
		int quantaRemaining = ((Integer) state.getValue(LEVEL)) + 1;

		// Flow vertically if possible
		int prevRemaining = quantaRemaining;
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
