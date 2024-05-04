package choonster.testmod3.fluid;

import choonster.testmod3.serialization.VanillaCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a snapshot of an {@link IFluidTank}, capturing the {@link FluidStack} contents and the capacity.
 *
 * @author Choonster
 */
public record FluidTankSnapshot(FluidStack contents, int capacity) {
	public static StreamCodec<RegistryFriendlyByteBuf, FluidTankSnapshot> STREAM_CODEC = StreamCodec.composite(
			VanillaCodecs.FLUID_STACK_OPTIONAL_STREAM_CODEC,
			FluidTankSnapshot::contents,
			ByteBufCodecs.VAR_INT,
			FluidTankSnapshot::capacity,
			FluidTankSnapshot::new
	);

	public static StreamCodec<RegistryFriendlyByteBuf, List<FluidTankSnapshot>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

	/**
	 * Creates an array of {@link FluidTankSnapshot}s from an {@link IFluidHandler}.
	 *
	 * @param fluidHandler The fluid handler
	 * @return The snapshots
	 */
	public static List<FluidTankSnapshot> getSnapshotsFromFluidHandler(final IFluidHandler fluidHandler) {
		final var numTanks = fluidHandler.getTanks();
		final var fluidTankSnapshots = new ArrayList<FluidTankSnapshot>(numTanks);

		for (var i = 0; i < numTanks; i++) {
			fluidTankSnapshots.add(new FluidTankSnapshot(fluidHandler.getFluidInTank(i), fluidHandler.getTankCapacity(i)));
		}

		return fluidTankSnapshots;
	}
}
