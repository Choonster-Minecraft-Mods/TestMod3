package choonster.testmod3.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * Utility methods for {@link Fluid}s.
 *
 * @author Choonster
 */
public class ModFluidUtil {
	/**
	 * Does the specified fluid have a bucket item registered for it?
	 *
	 * @param fluidStack The fluid
	 * @return true if the fluid has a bucket and its default state is a source; otherwise, false
	 */
	public static boolean hasBucket(final FluidStack fluidStack) {
		return !fluidStack.getFluid().getAttributes().getBucket(fluidStack).isEmpty() &&
				fluidStack.getFluid().defaultFluidState().isSource();
	}

	/**
	 * Fills a fluid container item with the specified fluid.
	 * <p>
	 * The entire {@code fluidStack} must be transferred into the fluid container for the operation to succeed,
	 * partial or zero transfers will fail.
	 *
	 * @param container  The container to be filled, will not be modified
	 * @param fluidStack The fluid to fill the container with
	 * @return A FluidActionResult holding the filled container if successful
	 */
	public static FluidActionResult fillContainer(final ItemStack container, final FluidStack fluidStack) {
		final ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1); // Don't modify the input

		final IFluidHandlerItem fluidHandler = FluidUtil
				.getFluidHandler(containerCopy)
				.orElseThrow(CapabilityNotPresentException::new);

		final int originalAmount = fluidStack.getAmount();

		final int amountFilled = fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

		if (amountFilled != originalAmount) {
			return FluidActionResult.FAILURE;
		}

		return new FluidActionResult(fluidHandler.getContainer());
	}

	/**
	 * Drains a fluid container item of the specified fluid.
	 * <p>
	 * The entire {@code fluidStack} must be transferred from the fluid container for the operation to succeed,
	 * partial or zero transfers will fail.
	 *
	 * @param container  The container to be drained, will not be modified
	 * @param fluidStack The fluid to drain from the container
	 * @return A FluidActionResult holding the drained container if successful
	 */
	public static FluidActionResult drainContainer(final ItemStack container, final FluidStack fluidStack) {
		final ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1); // Don't modify the input

		final IFluidHandlerItem fluidHandler = FluidUtil
				.getFluidHandler(containerCopy)
				.orElseThrow(CapabilityNotPresentException::new);

		final int originalAmount = fluidStack.getAmount();

		final FluidStack fluidDrained = fluidHandler.drain(fluidStack, IFluidHandler.FluidAction.EXECUTE);

		if (fluidDrained.getAmount() != originalAmount) {
			return FluidActionResult.FAILURE;
		}

		return new FluidActionResult(fluidHandler.getContainer());
	}
}
