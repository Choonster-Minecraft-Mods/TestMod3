package choonster.testmod3.network.capability.fluidhandler;

import choonster.testmod3.network.capability.BulkUpdateContainerCapabilityMessage;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
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
public class BulkUpdateContainerFluidTanksMessage extends BulkUpdateContainerCapabilityMessage<IFluidHandlerItem, FluidTankInfo> {
	public BulkUpdateContainerFluidTanksMessage(
			@Nullable final Direction facing,
			final int windowID,
			final NonNullList<ItemStack> items
	) {
		super(
				CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
				facing, windowID, items,
				FluidHandlerFunctions::convertFluidHandlerToFluidTankInfo
		);
	}

	private BulkUpdateContainerFluidTanksMessage(
			@Nullable final Direction facing,
			final int windowID,
			final Int2ObjectMap<FluidTankInfo> capabilityData
	) {
		super(
				CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
				facing, windowID, capabilityData
		);
	}

	public static BulkUpdateContainerFluidTanksMessage decode(final PacketBuffer buffer) {
		return BulkUpdateContainerCapabilityMessage.<IFluidHandlerItem, FluidTankInfo, BulkUpdateContainerFluidTanksMessage>decode(
				buffer,
				FluidHandlerFunctions::decodeFluidTankInfo,
				BulkUpdateContainerFluidTanksMessage::new
		);
	}

	public static void encode(final BulkUpdateContainerFluidTanksMessage message, final PacketBuffer buffer) {
		BulkUpdateContainerCapabilityMessage.encode(
				message,
				buffer,
				FluidHandlerFunctions::encodeFluidTankInfo
		);
	}

	public static void handle(final BulkUpdateContainerFluidTanksMessage message, final Supplier<NetworkEvent.Context> ctx) {
		BulkUpdateContainerCapabilityMessage.handle(
				message,
				ctx,
				FluidHandlerFunctions::applyFluidTankInfoToFluidTank
		);
	}
}
