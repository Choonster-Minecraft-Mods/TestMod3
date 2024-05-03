package choonster.testmod3.network.capability.fluidhandler;

import choonster.testmod3.fluid.FluidTankSnapshot;
import choonster.testmod3.serialization.VanillaCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * Functions used by the {@link IFluidHandlerItem} capability update message.
 *
 * @author Choonster
 */
class FluidHandlerFunctions {
	static FluidTankSnapshot convertFluidHandlerToFluidTankSnapshot(final IFluidHandlerItem fluidHandlerItem) {
		return new FluidTankSnapshot(fluidHandlerItem.getFluidInTank(0), fluidHandlerItem.getTankCapacity(0));
	}

	static FluidTankSnapshot decodeFluidTankSnapshot(final RegistryFriendlyByteBuf buffer) {
		final var contents = VanillaCodecs.FLUID_STACK_OPTIONAL_STREAM_CODEC.decode(buffer);
		final var capacity = buffer.readInt();

		return new FluidTankSnapshot(contents, capacity);
	}

	static void encodeFluidTankSnapshot(final FluidTankSnapshot fluidTankSnapshot, final RegistryFriendlyByteBuf buffer) {
		final var contents = fluidTankSnapshot.contents();
		VanillaCodecs.FLUID_STACK_OPTIONAL_STREAM_CODEC.encode(buffer, contents);

		buffer.writeInt(fluidTankSnapshot.capacity());
	}

	static void applyFluidTankSnapshotToFluidTank(final IFluidHandlerItem fluidHandlerItem, final FluidTankSnapshot fluidTankSnapshot) {
		if (fluidHandlerItem instanceof FluidTank) {
			((FluidTank) fluidHandlerItem).setFluid(fluidTankSnapshot.contents());
		}
	}
}
