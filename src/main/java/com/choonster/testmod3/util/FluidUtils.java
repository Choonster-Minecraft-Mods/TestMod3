package com.choonster.testmod3.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

/**
 * Utility methods for filling and draining fluid container items.
 * <p>
 * The methods in this class do not modify the {@link FluidStack} or container {@link ItemStack} passed to them,
 * instead they return a {@link FluidStack} and an {@link ItemStack} as part of a {@link FluidUtils.FluidResult}.
 */
public class FluidUtils {

	/**
	 * Fill a fluid container from the specified {@link FluidStack}.
	 *
	 * @param fluidToFill The fluid to fill the container with
	 * @param container   The container item
	 * @return A {@link FluidUtils.FluidResult} describing the result of the operation
	 */
	public static FluidResult fillFluidContainer(FluidStack fluidToFill, ItemStack container) {
		fluidToFill = fluidToFill.copy(); // Copy the FluidStack so we can't modify the original

		if (container.getItem() instanceof IFluidContainerItem) {
			ItemStack newContainer = container.copy();
			IFluidContainerItem fluidContainerItem = (IFluidContainerItem) newContainer.getItem();
			if (fluidContainerItem.fill(newContainer, fluidToFill, false) > 0) {
				int amountFilled = fluidContainerItem.fill(newContainer, fluidToFill, true);
				FluidStack fluidFilled = new FluidStack(fluidToFill, amountFilled);
				return new FluidResult(FluidResult.Type.FILL, newContainer, fluidFilled);
			}
		} else if (FluidContainerRegistry.isEmptyContainer(container)) {
			ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(fluidToFill, container);
			if (filledContainer != null) {
				FluidStack fluidFilled = FluidContainerRegistry.getFluidForFilledItem(filledContainer);
				return new FluidResult(FluidResult.Type.FILL, filledContainer, fluidFilled);
			}
		}

		return FluidResult.RESULT_NOOP;
	}

	/**
	 * Is {@code fluidStack} non-{@code null} with an {@link FluidStack#amount} greater than 0?
	 *
	 * @param fluidStack The {@link FluidStack}
	 * @return Does the {@link FluidStack} have fluid in it?
	 */
	public static boolean fluidStackHasFluid(FluidStack fluidStack) {
		return fluidStack != null && fluidStack.amount > 0;
	}

	/**
	 * Drain a fluid container, optionally only draining a specific fluid.
	 *
	 * @param fluidToDrain   The fluid to drain from the container. If {@code null}, any fluid will be drained.
	 * @param maxDrainAmount The maximum amount of fluid to drain
	 * @param container      The container item
	 * @return A {@link FluidUtils.FluidResult} describing the result of the operation
	 */
	public static FluidResult drainFluidContainer(FluidStack fluidToDrain, int maxDrainAmount, ItemStack container) {
		fluidToDrain = fluidToDrain != null ? fluidToDrain.copy() : null; // Copy the FluidStack so we can't modify the original

		if (container.getItem() instanceof IFluidContainerItem) {
			ItemStack newContainer = container.copy();
			IFluidContainerItem fluidContainerItem = (IFluidContainerItem) newContainer.getItem();
			FluidStack fluidInContainer = fluidContainerItem.getFluid(newContainer);
			if ((fluidToDrain == null || fluidToDrain.isFluidEqual(fluidInContainer)) && (fluidInContainer == null || maxDrainAmount >= fluidInContainer.amount)) {
				if (fluidStackHasFluid(fluidContainerItem.drain(newContainer, maxDrainAmount, false))) {
					FluidStack fluidDrained = fluidContainerItem.drain(newContainer, maxDrainAmount, true);
					return new FluidResult(FluidResult.Type.DRAIN, newContainer, fluidDrained);
				}
			}
		} else if (FluidContainerRegistry.isFilledContainer(container)) {
			FluidStack fluidInContainer = FluidContainerRegistry.getFluidForFilledItem(container);
			if ((fluidToDrain == null || fluidToDrain.isFluidEqual(fluidInContainer)) && maxDrainAmount >= fluidInContainer.amount) {
				ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(container);
				if (emptyContainer != null) {
					return new FluidResult(FluidResult.Type.DRAIN, emptyContainer, fluidInContainer);
				}
			}
		}

		return FluidResult.RESULT_NOOP;
	}

	/**
	 * The result of a fluid fill or drain operation.
	 */
	public static class FluidResult {

		/**
		 * Returned from fluid operations to signify that no fluid was filled or drained.
		 */
		public static final FluidResult RESULT_NOOP = new FluidResult(FluidResult.Type.NONE, null, null);

		/**
		 * The new container item. Can be {@code null} if {@link #operationType} is {@link FluidUtils.FluidResult.Type#NONE}.
		 */
		public final ItemStack newContainer;

		/**
		 * The type and amount of fluid that was consumed. Can be {@code null} if {@link #operationType} is {@link FluidUtils.FluidResult.Type#NONE}.
		 */
		public final FluidStack fluidConsumed;

		/**
		 * The type of fluid operation.
		 */
		public final Type operationType;

		public FluidResult(Type operationType, ItemStack newContainer, FluidStack fluidConsumed) {
			this.newContainer = newContainer;
			this.operationType = operationType;
			this.fluidConsumed = fluidConsumed;
		}

		public enum Type {
			FILL,
			DRAIN,
			NONE
		}
	}
}
