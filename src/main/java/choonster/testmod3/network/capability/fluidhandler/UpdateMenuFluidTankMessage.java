package choonster.testmod3.network.capability.fluidhandler;

import choonster.testmod3.fluid.FluidTankSnapshot;
import choonster.testmod3.network.capability.UpdateMenuCapabilityMessage;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
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
public class UpdateMenuFluidTankMessage extends UpdateMenuCapabilityMessage<IFluidHandlerItem, FluidTankSnapshot> {
	public UpdateMenuFluidTankMessage(
			@Nullable final Direction facing,
			final int containerID,
			final int stateID,
			final int slotNumber,
			final IFluidHandlerItem fluidHandler
	) {
		super(
				ForgeCapabilities.FLUID_HANDLER_ITEM,
				facing, containerID, stateID, slotNumber, fluidHandler,
				FluidHandlerFunctions::convertFluidHandlerToFluidTankSnapshot
		);
	}

	private UpdateMenuFluidTankMessage(
			@Nullable final Direction facing,
			final int containerID,
			final int stateID,
			final int slotNumber,
			final FluidTankSnapshot capabilityData
	) {
		super(
				ForgeCapabilities.FLUID_HANDLER_ITEM,
				facing, containerID, stateID, slotNumber, capabilityData
		);
	}

	public static UpdateMenuFluidTankMessage decode(final FriendlyByteBuf buffer) {
		return UpdateMenuCapabilityMessage.<IFluidHandlerItem, FluidTankSnapshot, UpdateMenuFluidTankMessage>decode(
				buffer,
				FluidHandlerFunctions::decodeFluidTankSnapshot,
				UpdateMenuFluidTankMessage::new
		);
	}

	public static void encode(final UpdateMenuFluidTankMessage message, final FriendlyByteBuf buffer) {
		UpdateMenuCapabilityMessage.encode(
				message,
				buffer,
				FluidHandlerFunctions::encodeFluidTankSnapshot
		);
	}

	public static void handle(final UpdateMenuFluidTankMessage message, final CustomPayloadEvent.Context ctx) {
		UpdateMenuCapabilityMessage.handle(
				message,
				ctx,
				FluidHandlerFunctions::applyFluidTankSnapshotToFluidTank
		);
	}
}
