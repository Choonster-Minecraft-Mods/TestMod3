package choonster.testmod3.network.capability.fluidhandler;

import net.minecraft.nbt.NBTTagCompound;
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
		final NBTTagCompound tagCompound = buffer.readCompoundTag();
		final FluidStack contents = FluidStack.loadFluidStackFromNBT(tagCompound);

		final int capacity = buffer.readInt();

		return new FluidTankInfo(contents, capacity);
	}

	static void encodeFluidTankInfo(final FluidTankInfo fluidTankInfo, final PacketBuffer buffer) {
		final FluidStack contents = fluidTankInfo.fluid;
		final NBTTagCompound tagCompound = new NBTTagCompound();

		if (contents != null) {
			contents.writeToNBT(tagCompound);
		}

		buffer.writeCompoundTag(tagCompound);

		buffer.writeInt(fluidTankInfo.capacity);
	}

	static void applyFluidTankInfoToFluidTank(final IFluidHandlerItem fluidHandlerItem, final FluidTankInfo fluidTankInfo) {
		if (fluidHandlerItem instanceof FluidTank) {
			((FluidTank) fluidHandlerItem).setFluid(fluidTankInfo.fluid);
		}
	}
}
