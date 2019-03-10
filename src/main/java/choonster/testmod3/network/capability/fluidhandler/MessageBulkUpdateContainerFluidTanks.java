package choonster.testmod3.network.capability.fluidhandler;

import choonster.testmod3.network.capability.MessageBulkUpdateContainerCapability;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Updates the {@link FluidTank} for each slot of a {@link Container}.
 *
 * @author Choonster
 */
public class MessageBulkUpdateContainerFluidTanks extends MessageBulkUpdateContainerCapability<IFluidHandlerItem, FluidTankInfo> {
	public MessageBulkUpdateContainerFluidTanks(
			@Nullable final EnumFacing facing,
			final int windowID,
			final NonNullList<ItemStack> items
	) {
		super(
				CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
				facing, windowID, items,
				FluidHandlerFunctions::convertFluidHandlerToFluidTankInfo
		);
	}

	private MessageBulkUpdateContainerFluidTanks(
			@Nullable final EnumFacing facing,
			final int windowID,
			final Int2ObjectMap<FluidTankInfo> capabilityData
	) {
		super(
				CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
				facing, windowID, capabilityData
		);
	}

	public static MessageBulkUpdateContainerFluidTanks decode(final PacketBuffer buffer) {
		return MessageBulkUpdateContainerCapability.decode(
				buffer,
				FluidHandlerFunctions::decodeFluidTankInfo,
				MessageBulkUpdateContainerFluidTanks::new
		);
	}

	public static void encode(final MessageBulkUpdateContainerFluidTanks message, final PacketBuffer buffer) {
		MessageBulkUpdateContainerCapability.encode(
				message,
				buffer,
				FluidHandlerFunctions::encodeFluidTankInfo
		);
	}

	public static void handle(final MessageBulkUpdateContainerFluidTanks message, final Supplier<NetworkEvent.Context> ctx) {
		MessageBulkUpdateContainerCapability.handle(
				message,
				ctx,
				FluidHandlerFunctions::applyFluidTankInfoToFluidTank
		);
	}
}
