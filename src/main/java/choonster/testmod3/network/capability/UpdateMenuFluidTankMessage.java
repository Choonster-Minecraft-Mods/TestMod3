package choonster.testmod3.network.capability;

import choonster.testmod3.fluid.FluidTankSnapshot;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

/**
 * Updates the {@link FluidTank} for a single slot of an {@link AbstractContainerMenu}.
 *
 * @author Choonster
 */
public record UpdateMenuFluidTankMessage(
		UpdateMenuCapabilityData<IFluidHandlerItem, FluidTankSnapshot> data
) implements UpdateMenuCapabilityData.UpdateMenuCapabilityDataMessage<IFluidHandlerItem, FluidTankSnapshot> {
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateMenuFluidTankMessage> STREAM_CODEC =
			UpdateMenuCapabilityData.<IFluidHandlerItem, FluidTankSnapshot, UpdateMenuFluidTankMessage>streamCodec(
					ForgeCapabilities.FLUID_HANDLER_ITEM,
					FluidTankSnapshot.STREAM_CODEC,
					UpdateMenuFluidTankMessage::new
			);

	public UpdateMenuFluidTankMessage(
			@Nullable final Direction direction,
			final int containerID,
			final int stateID,
			final int slotNumber,
			final IFluidHandlerItem fluidHandler
	) {
		this(new UpdateMenuCapabilityData<>(
				ForgeCapabilities.FLUID_HANDLER_ITEM,
				direction, containerID, stateID, slotNumber, fluidHandler,
				fluidHandlerItem -> new FluidTankSnapshot(fluidHandlerItem.getFluidInTank(0), fluidHandlerItem.getTankCapacity(0))
		));
	}

	public static void handle(final UpdateMenuFluidTankMessage message, final CustomPayloadEvent.Context ctx) {
		message.data.handle((fluidHandlerItem, fluidTankSnapshot) -> {
			if (fluidHandlerItem instanceof final FluidTank fluidTank) {
				fluidTank.setFluid(fluidTankSnapshot.contents());
			}
		});
	}

}
