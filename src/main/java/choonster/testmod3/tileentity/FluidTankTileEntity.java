package choonster.testmod3.tileentity;

import choonster.testmod3.init.ModTileEntities;
import net.minecraftforge.fluids.Fluid;

/**
 * A tank that holds 10 buckets of fluid.
 *
 * @author Choonster
 */
public class FluidTankTileEntity extends BaseFluidTankTileEntity {
	public static final int CAPACITY = 10 * Fluid.BUCKET_VOLUME;

	public FluidTankTileEntity() {
		super(ModTileEntities.FLUID_TANK);
		tank = new choonster.testmod3.fluids.TileEntityFluidTank(this, CAPACITY);
	}
}
