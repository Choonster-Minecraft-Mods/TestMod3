package choonster.testmod3.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

/**
 * An {@link IFluidHandlerItem} implementation that only allows complete filling/draining and can only be filled with
 * Water, Lava, Milk and {@link Fluid}s registered for the Universal Bucket.
 * <p>
 * Ignores {_@link FluidRegistry#isUniversalBucketEnabled()}, unlike {@link FluidBucketWrapper}.
 *
 * @author Choonster
 */
public class UniversalBucketFluidHandler extends FluidHandlerItemStackSimple {
	public UniversalBucketFluidHandler(final ItemStack container, final int capacity) {
		super(container, capacity);
	}

	@Override
	public boolean canFillFluidType(final FluidStack fluid) {
		return true;

		/*
		// TODO: Uncomment when fluids are reimplemented in 1.14
		return fluid.getFluid() == FluidRegistry.WATER ||
				fluid.getFluid() == FluidRegistry.LAVA ||
				fluid.getFluid().getName().equals("milk") ||
				FluidRegistry.getBucketFluids().contains(fluid.getFluid());
		*/
	}
}
