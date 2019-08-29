package choonster.testmod3.network.capability.fluidhandler;

import choonster.testmod3.fluids.FluidTankSnapshot;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * Functions used by the {@link IFluidHandlerItem} capability update messages.
 *
 * @author Choonster
 */
class FluidHandlerFunctions {
	static FluidTankSnapshot convertFluidHandlerToFluidTankSnapshot(final IFluidHandlerItem fluidHandlerItem) {
		return new FluidTankSnapshot(fluidHandlerItem.getFluidInTank(0), fluidHandlerItem.getTankCapacity(0));
	}

	static FluidTankSnapshot decodeFluidTankSnapshot(final PacketBuffer buffer) {
		final FluidStack contents = FluidStack.readFromPacket(buffer);
		final int capacity = buffer.readInt();

		return new FluidTankSnapshot(contents, capacity);
	}

	static void encodeFluidTankSnapshot(final FluidTankSnapshot fluidTankSnapshot, final PacketBuffer buffer) {
		final FluidStack contents = fluidTankSnapshot.getContents();
		contents.writeToPacket(buffer);

		buffer.writeInt(fluidTankSnapshot.getCapacity());
	}

	static void applyFluidTankSnapshotToFluidTank(final IFluidHandlerItem fluidHandlerItem, final FluidTankSnapshot fluidTankSnapshot) {
		if (fluidHandlerItem instanceof FluidTank) {
			((FluidTank) fluidHandlerItem).setFluid(fluidTankSnapshot.getContents());
		}
	}
}
