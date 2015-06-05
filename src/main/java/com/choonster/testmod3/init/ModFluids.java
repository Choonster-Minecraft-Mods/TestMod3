package com.choonster.testmod3.init;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {
	public static Fluid staticFluid;

	public static void registerFluids() {
		staticFluid = registerFluid(new Fluid("static").setLuminosity(10).setDensity(800).setViscosity(1500));
	}

	private static <T extends Fluid> T registerFluid(T fluid) {
		if (!FluidRegistry.registerFluid(fluid)) {
			throw new IllegalStateException(String.format("Unable to register fluid %s", fluid.getID()));
		}

		return fluid;
	}
}
