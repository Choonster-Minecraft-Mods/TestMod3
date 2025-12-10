package choonster.testmod3.fluid;

import choonster.testmod3.init.ModDataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jspecify.annotations.Nullable;

import java.util.function.Predicate;

/**
 * An implementation of {@link IFluidHandlerItem} that stores its contents as a {@link FluidStack} component.
 *
 * @author Choonster
 */
public class ItemFluidTank extends BaseFluidTank implements IFluidHandlerItem {
	private final ItemStack container;

	public ItemFluidTank(final int capacity, final ItemStack container) {
		super(capacity);
		this.container = container;
	}

	public ItemFluidTank(final int capacity, final Predicate<FluidStack> validator, final ItemStack container) {
		super(capacity, validator);
		this.container = container;
	}

	@Override
	public ItemStack getContainer() {
		return container;
	}

	@Override
	public FluidStack getFluid() {
		return container.getOrDefault(ModDataComponents.CONTAINED_FLUID.get(), FluidStack.EMPTY);
	}

	@Override
	protected void setFluid(final FluidStack stack) {
		container.set(ModDataComponents.CONTAINED_FLUID.get(), stack);
	}

	@Override
	public int fill(final FluidStack resource, final FluidAction action) {
		if (container.getCount() != 1) {
			return 0;
		}

		return super.fill(resource, action);
	}

	@Override
	public FluidStack drain(final FluidStack resource, final FluidAction action) {
		if (container.getCount() != 1) {
			return FluidStack.EMPTY;
		}

		return super.drain(resource, action);
	}

	@Override
	public FluidStack drain(final int maxDrain, final FluidAction action) {
		if (container.getCount() != 1 || maxDrain <= 0) {
			return FluidStack.EMPTY;
		}

		return super.drain(maxDrain, action);
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		final var that = ((ItemFluidTank) obj);

		return getFluid().equals(that.getFluid());
	}

	@Override
	public int hashCode() {
		return getFluid().hashCode();
	}
}
