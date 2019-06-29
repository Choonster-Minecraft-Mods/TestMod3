package choonster.testmod3.network.capability.fluidhandler;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;

/**
 * Functions used by the {@link IFluidHandlerItem} capability update messages.
 *
 * @author Choonster
 */
class FluidHandlerFunctions {
	@Nullable
	static FluidTankInfo convertFluidHandlerToFluidTankInfo(final IFluidHandlerItem fluidHandlerItem) {
		if (fluidHandlerItem instanceof FluidTank) {
			return ((FluidTank) fluidHandlerItem).getInfo();
		} else {
			return null;
		}
	}

	static FluidTankInfo decodeFluidTankInfo(final PacketBuffer buffer) {
		final CompoundNBT compoundTag = buffer.readCompoundTag();
		final FluidStack contents = FluidStack.loadFluidStackFromNBT(compoundTag);

		final int capacity = buffer.readInt();

		return new FluidTankInfo(contents, capacity);
	}

	static void encodeFluidTankInfo(final FluidTankInfo fluidTankInfo, final PacketBuffer buffer) {
		final FluidStack contents = fluidTankInfo.fluid;
		final CompoundNBT compoundTag = new CompoundNBT();

		if (contents != null) {
			contents.writeToNBT(compoundTag);
		}

		buffer.writeCompoundTag(compoundTag);

		buffer.writeInt(fluidTankInfo.capacity);
	}

	static void applyFluidTankInfoToFluidTank(final IFluidHandlerItem fluidHandlerItem, final FluidTankInfo fluidTankInfo) {
		if (fluidHandlerItem instanceof FluidTank) {
			((FluidTank) fluidHandlerItem).setFluid(fluidTankInfo.fluid);
		}
	}
}
