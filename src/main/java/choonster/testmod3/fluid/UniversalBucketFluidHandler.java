package choonster.testmod3.fluid;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

/**
 * An {@link IFluidHandlerItem} implementation that only allows complete filling/draining and can only be filled with
 * fluids that have a bucket registered ({@link FluidAttributes#getBucket(FluidStack)}).
 * <p>
 * The fluid is stored as a {@link FluidStack} in-memory rather than being written to the {@link ItemStack}'s NBT.
 *
 * @author Choonster
 */
public class UniversalBucketFluidHandler extends FluidHandlerItemStackSimple {
	private FluidStack fluid = FluidStack.EMPTY;

	public UniversalBucketFluidHandler(final ItemStack container, final int capacity) {
		super(container, capacity);
	}

	@Override
	public FluidStack getFluid() {
		return fluid;
	}

	@Override
	protected void setFluid(final FluidStack fluid) {
		this.fluid = fluid;
	}

	@Override
	protected void setContainerToEmpty() {
		fluid = FluidStack.EMPTY;
	}
}
