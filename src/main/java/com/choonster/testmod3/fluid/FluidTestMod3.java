package com.choonster.testmod3.fluid;

import net.minecraftforge.fluids.Fluid;

public class FluidTestMod3 extends Fluid {
	private boolean hasFlowIcon;

	public FluidTestMod3(String fluidName) {
		this(fluidName, true);
	}

	public FluidTestMod3(String fluidName, boolean hasFlowIcon) {
		super(fluidName);
		this.hasFlowIcon = hasFlowIcon;
	}

	public boolean hasFlowIcon() {
		return hasFlowIcon;
	}
}
