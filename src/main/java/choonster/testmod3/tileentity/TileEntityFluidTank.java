package choonster.testmod3.tileentity;

import choonster.testmod3.fluids.FluidTankWithTile;
import choonster.testmod3.init.ModTileEntities;
import net.minecraftforge.fluids.Fluid;

/**
 * A tank that holds 10 buckets of fluid.
 *
 * @author Choonster
 */
public class TileEntityFluidTank extends TileEntityFluidTankBase {
	public static final int CAPACITY = 10 * Fluid.BUCKET_VOLUME;

	public TileEntityFluidTank() {
		super(ModTileEntities.FLUID_TANK);
		tank = new FluidTankWithTile(this, CAPACITY);
	}
}
