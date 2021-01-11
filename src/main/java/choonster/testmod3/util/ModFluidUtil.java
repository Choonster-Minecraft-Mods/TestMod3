package choonster.testmod3.util;

import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * Utility methods for {@link Fluid}s.
 *
 * @author Choonster
 */
public class ModFluidUtil {
	public static boolean hasBucket(final FluidStack fluidStack) {
		return !fluidStack.getFluid().getAttributes().getBucket(fluidStack).isEmpty() &&
				fluidStack.getFluid().getDefaultState().isSource();
	}
}
