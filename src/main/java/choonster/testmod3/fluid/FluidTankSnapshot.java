package choonster.testmod3.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Represents a snapshot of an {@link IFluidTank}, capturing the {@link FluidStack} contents and the capacity.
 *
 * @author Choonster
 */
public class FluidTankSnapshot {
	private final FluidStack contents;
	private final int capacity;

	public FluidTankSnapshot(final FluidStack contents, final int capacity) {
		this.contents = contents;
		this.capacity = capacity;
	}

	/**
	 * Gets the contents of the tank.
	 *
	 * @return The contents
	 */
	public FluidStack getContents() {
		return contents;
	}

	/**
	 * Gets the capacity of the tank.
	 *
	 * @return The capacity
	 */
	public int getCapacity() {
		return capacity;
	}

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
