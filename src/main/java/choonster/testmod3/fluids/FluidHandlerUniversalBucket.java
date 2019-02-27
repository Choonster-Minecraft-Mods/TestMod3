package choonster.testmod3.fluids;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

/**
 * An {@link IFluidHandlerItem} implementation that only allows complete filling/draining and can only be filled with
 * Water, Lava, Milk and {@link Fluid}s registered for the Universal Bucket.
 * <p>
 * Ignores {@link FluidRegistry#isUniversalBucketEnabled()}, unlike {@link FluidBucketWrapper}.
 *
 * @author Choonster
 */
public class FluidHandlerUniversalBucket extends FluidHandlerItemStackSimple {
	public FluidHandlerUniversalBucket(final ItemStack container, final int capacity) {
		super(container, capacity);
	}

	@Override
	public boolean canFillFluidType(final FluidStack fluid) {
		return true;

		/*
		// TODO: Uncomment when fluid registry is reimplemented
		return fluid.getFluid() == FluidRegistry.WATER ||
				fluid.getFluid() == FluidRegistry.LAVA ||
				fluid.getFluid().getName().equals("milk") ||
				FluidRegistry.getBucketFluids().contains(fluid.getFluid());
		*/
	}
}
