package choonster.testmod3.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Represents a snapshot of an {@link IFluidTank}, capturing the {@link FluidStack} contents and the capacity.
 *
 * @author Choonster
 */
public record FluidTankSnapshot(FluidStack contents, int capacity) {
	/**
	 * Creates an array of {@link FluidTankSnapshot}s from an {@link IFluidHandler}.
	 *
	 * @param fluidHandler The fluid handler
	 * @return The snapshots
	 */
	public static FluidTankSnapshot[] getSnapshotsFromFluidHandler(final IFluidHandler fluidHandler) {
		final FluidTankSnapshot[] fluidTankSnapshots = new FluidTankSnapshot[fluidHandler.getTanks()];

		for (int i = 0; i < fluidTankSnapshots.length; i++) {
			fluidTankSnapshots[i] = new FluidTankSnapshot(fluidHandler.getFluidInTank(i), fluidHandler.getTankCapacity(i));
		}

		return fluidTankSnapshots;
	}
}
