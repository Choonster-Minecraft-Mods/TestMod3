package com.choonster.testmod3.block.fluid;

import net.minecraftforge.fluids.IFluidBlock;

public interface IFluidBlockWithModel extends IFluidBlock {
	int getDensityDir();
}
