package choonster.testmod3.fluid;

import choonster.testmod3.util.ModFluidUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

/**
 * An {@link IFluidHandlerItem} implementation that only allows complete filling/draining and can only be filled with
 * fluids that have a bucket registered ({@link FluidAttributes#getBucket(FluidStack)}).
 *
 * @author Choonster
 */
public class UniversalBucketFluidHandler extends ItemFluidTank {
	public UniversalBucketFluidHandler(final ItemStack container, final int capacity) {
		super(container, capacity);
		setValidator(ModFluidUtil::hasBucket);
	}

	@Override
	public int fill(final FluidStack resource, final FluidAction action) {
		// Simulate the fill to see how much would be filled
		final int amountToBeFilled = super.fill(resource, FluidAction.SIMULATE);

		// If it's not equal to the bucket's capacity, don't allow the fill
		if (amountToBeFilled != getCapacity()) {
			return 0;
		}

		// If this is a simulate request, return the result from the super method
		if (action.simulate()) {
			return amountToBeFilled;
		}

		// Otherwise, call the super method to execute the fill
		return super.fill(resource, FluidAction.EXECUTE);
	}

	@Override
	public FluidStack drain(final int maxDrain, final FluidAction action) {
		// Simulate the drain to see how much would be drained
		final FluidStack fluidToBeDrained = super.drain(maxDrain, FluidAction.SIMULATE);

		// If it's not equal to the bucket's capacity, don't allow the drain
		if (fluidToBeDrained.getAmount() != getCapacity()) {
			return FluidStack.EMPTY;
		}

		// If this is a simulate request, return the result from the super method
		if (action.simulate()) {
			return fluidToBeDrained;
		}

		// Otherwise, call the super method to execute the drain
		return super.drain(maxDrain, FluidAction.EXECUTE);
	}
}
