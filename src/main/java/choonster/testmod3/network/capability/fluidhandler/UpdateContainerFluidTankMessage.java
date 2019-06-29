package choonster.testmod3.network.capability.fluidhandler;

import choonster.testmod3.network.capability.UpdateContainerCapabilityMessage;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Updates the {@link FluidTank} for a single slot of a {@link Container}.
 *
 * @author Choonster
 */
public class UpdateContainerFluidTankMessage extends UpdateContainerCapabilityMessage<IFluidHandlerItem, FluidTankInfo> {
	public UpdateContainerFluidTankMessage(
			@Nullable final Direction facing,
			final int windowID,
			final int slotNumber,
			final IFluidHandlerItem fluidHandler
	) {
		super(
				CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
				facing, windowID, slotNumber, fluidHandler,
				FluidHandlerFunctions::convertFluidHandlerToFluidTankInfo
		);
	}

	private UpdateContainerFluidTankMessage(
			@Nullable final Direction facing,
			final int windowID,
			final int slotNumber,
			final FluidTankInfo capabilityData
	) {
		super(
				CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
				facing, windowID, slotNumber, capabilityData
		);
	}

	public static UpdateContainerFluidTankMessage decode(final PacketBuffer buffer) {
		return UpdateContainerCapabilityMessage.<IFluidHandlerItem, FluidTankInfo, UpdateContainerFluidTankMessage>decode(
				buffer,
				FluidHandlerFunctions::decodeFluidTankInfo,
				UpdateContainerFluidTankMessage::new
		);
	}

	public static void encode(final UpdateContainerFluidTankMessage message, final PacketBuffer buffer) {
		UpdateContainerCapabilityMessage.encode(
				message,
				buffer,
				FluidHandlerFunctions::encodeFluidTankInfo
		);
	}

	public static void handle(final UpdateContainerFluidTankMessage message, final Supplier<NetworkEvent.Context> ctx) {
		UpdateContainerCapabilityMessage.handle(
				message,
				ctx,
				FluidHandlerFunctions::applyFluidTankInfoToFluidTank
		);
	}
}
