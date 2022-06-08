package choonster.testmod3.fluid;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of {@link IFluidHandlerItem} that stores its contents as a {@link FluidStack} in memory rather than
 * storing them in the vanilla {@link ItemStack} NBT.
 *
 * @author Choonster
 */
public class ItemFluidTank extends FluidTank implements IFluidHandlerItem, INBTSerializable<CompoundTag> {
	private final ItemStack container;

	public ItemFluidTank(final ItemStack container, final int capacity) {
		super(capacity);
		this.container = container;
	}

	/**
	 * Get the container currently acted on by this fluid handler.
	 * The ItemStack may be different from its initial state, in the case of fluid containers that have different items
	 * for their filled and empty states.
	 * May be an empty item if the container was drained and is consumable.
	 */
	@Override
	public ItemStack getContainer() {
		return container;
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

		final FluidTank that = ((FluidTank) obj);

		return getFluid().equals(that.getFluid());
	}

	@Override
	public int hashCode() {
		return fluid.hashCode();
	}

	@Override
	public CompoundTag serializeNBT() {
		return writeToNBT(new CompoundTag());
	}

	@Override
	public void deserializeNBT(final CompoundTag tag) {
		readFromNBT(tag);
	}
}
