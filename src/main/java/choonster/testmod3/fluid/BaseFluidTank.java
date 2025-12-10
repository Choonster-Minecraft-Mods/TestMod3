package choonster.testmod3.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

/**
 * Base fluid tank implementation that can store the fluid externally.
 * <p>
 * Adapted from {@link FluidTank}.
 *
 * @author Choonster
 */
public abstract class BaseFluidTank implements IFluidHandler, IFluidTank {
	protected final Predicate<FluidStack> validator;
	protected final int capacity;

	public BaseFluidTank(final int capacity) {
		this(capacity, f -> true);
	}

	public BaseFluidTank(final int capacity, final Predicate<FluidStack> validator) {
		this.validator = validator;
		this.capacity = capacity;
	}

	@Override
	public boolean isFluidValid(final FluidStack stack) {
		return validator.test(stack);
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public abstract FluidStack getFluid();

	protected abstract void setFluid(final FluidStack stack);

	@Override
	public int getFluidAmount() {
		return getFluid().getAmount();
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public FluidStack getFluidInTank(final int tank) {
		return getFluid();
	}

	@Override
	public int getTankCapacity(final int tank) {
		return getCapacity();
	}

	@Override
	public boolean isFluidValid(final int tank, final FluidStack stack) {
		return isFluidValid(stack);
	}

	@Override
	public int fill(final FluidStack resource, final FluidAction action) {
		if (resource.isEmpty() || !isFluidValid(resource)) {
			return 0;
		}

		final var fluid = getFluid();

		if (action.simulate()) {
			if (fluid.isEmpty()) {
				return Math.min(capacity, resource.getAmount());
			}

			if (!fluid.isFluidEqual(resource)) {
				return 0;
			}

			return Math.min(capacity - fluid.getAmount(), resource.getAmount());
		}

		if (fluid.isEmpty()) {
			final var newFluid = new FluidStack(resource, Math.min(capacity, resource.getAmount()));
			setFluid(newFluid);
			onContentsChanged();
			return newFluid.getAmount();
		}

		if (!fluid.isFluidEqual(resource)) {
			return 0;
		}

		var filled = capacity - fluid.getAmount();

		if (resource.getAmount() < filled) {
			final var newFluid = fluid.copy();
			newFluid.grow(resource.getAmount());
			setFluid(newFluid);

			filled = resource.getAmount();
		} else {
			final var newFluid = fluid.copy();
			newFluid.setAmount(capacity);
			setFluid(newFluid);

			setFluid(fluid);
		}

		if (filled > 0) {
			onContentsChanged();
		}

		return filled;
	}

	@Override
	public FluidStack drain(final FluidStack resource, final FluidAction action) {
		if (resource.isEmpty() || !resource.isFluidEqual(getFluid())) {
			return FluidStack.EMPTY;
		}

		return drain(resource.getAmount(), action);
	}

	@Override
	public FluidStack drain(final int maxDrain, final FluidAction action) {
		final var fluid = getFluid();
		var drained = maxDrain;

		if (fluid.getAmount() < drained) {
			drained = fluid.getAmount();
		}

		final var stack = new FluidStack(fluid, drained);
		if (action.execute() && drained > 0) {
			final var newFluid = fluid.copy();
			newFluid.shrink(drained);
			setFluid(newFluid);

			onContentsChanged();
		}

		return stack;
	}

	protected void onContentsChanged() {
	}

	public boolean isEmpty() {
		return getFluid().isEmpty();
	}

	public int getSpace() {
		return Math.max(0, capacity - getFluid().getAmount());
	}
}