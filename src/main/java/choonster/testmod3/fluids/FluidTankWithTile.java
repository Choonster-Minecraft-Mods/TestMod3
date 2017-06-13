package choonster.testmod3.fluids;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent.FluidDrainingEvent;
import net.minecraftforge.fluids.FluidEvent.FluidFillingEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

/**
 * An extension of {@link FluidTank} that sets the {@link FluidTank#tile} field, so {@link FluidDrainingEvent} and {@link FluidFillingEvent} are fired.
 *
 * @author Choonster
 */
public class FluidTankWithTile extends FluidTank {
	public FluidTankWithTile(final TileEntity tileEntity, final int capacity) {
		super(capacity);
		tile = tileEntity;
	}

	public FluidTankWithTile(final TileEntity tileEntity, final FluidStack stack, final int capacity) {
		super(stack, capacity);
		tile = tileEntity;
	}

	public FluidTankWithTile(final TileEntity tileEntity, final Fluid fluid, final int amount, final int capacity) {
		super(fluid, amount, capacity);
		tile = tileEntity;
	}
}
