package choonster.testmod3.network.capability.fluidhandler;

import choonster.testmod3.network.capability.MessageUpdateContainerCapability;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
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
public class MessageUpdateContainerFluidTank extends MessageUpdateContainerCapability<IFluidHandlerItem, FluidTankInfo> {
	public MessageUpdateContainerFluidTank(
			@Nullable final EnumFacing facing,
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

	private MessageUpdateContainerFluidTank(
			@Nullable final EnumFacing facing,
			final int windowID,
			final int slotNumber,
			final FluidTankInfo capabilityData
	) {
		super(
				CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
				facing, windowID, slotNumber, capabilityData
		);
	}

	public static MessageUpdateContainerFluidTank decode(final PacketBuffer buffer) {
		return MessageUpdateContainerCapability.<IFluidHandlerItem, FluidTankInfo, MessageUpdateContainerFluidTank>decode(
				buffer,
				FluidHandlerFunctions::decodeFluidTankInfo,
				MessageUpdateContainerFluidTank::new
		);
	}

	public static void encode(final MessageUpdateContainerFluidTank message, final PacketBuffer buffer) {
		MessageUpdateContainerCapability.encode(
				message,
				buffer,
				FluidHandlerFunctions::encodeFluidTankInfo
		);
	}

	public static void handle(final MessageUpdateContainerFluidTank message, final Supplier<NetworkEvent.Context> ctx) {
		MessageUpdateContainerCapability.handle(
				message,
				ctx,
				FluidHandlerFunctions::applyFluidTankInfoToFluidTank
		);
	}
}
