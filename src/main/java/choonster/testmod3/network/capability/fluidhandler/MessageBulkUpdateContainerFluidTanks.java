package choonster.testmod3.network.capability.fluidhandler;

import choonster.testmod3.network.capability.MessageBulkUpdateContainerCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;

/**
 * Updates the {@link FluidTank} for each slot of a {@link Container}.
 *
 * @author Choonster
 */
public class MessageBulkUpdateContainerFluidTanks extends MessageBulkUpdateContainerCapability<IFluidHandlerItem, FluidTankInfo> {

	@SuppressWarnings("unused")
	public MessageBulkUpdateContainerFluidTanks() {
		super(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
	}

	public MessageBulkUpdateContainerFluidTanks(final int windowID, final NonNullList<ItemStack> items) {
		super(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null, windowID, items);
	}

	/**
	 * Convert a capability handler instance to a data instance.
	 *
	 * @param fluidHandlerItem The handler
	 * @return The data instance
	 */
	@Nullable
	@Override
	protected FluidTankInfo convertCapabilityToData(final IFluidHandlerItem fluidHandlerItem) {
		if (fluidHandlerItem instanceof FluidTank) {
			return ((FluidTank) fluidHandlerItem).getInfo();
		} else {
			return null;
		}
	}

	/**
	 * Read a data instance from the buffer.
	 *
	 * @param buf The buffer
	 */
	@Override
	protected FluidTankInfo readCapabilityData(final ByteBuf buf) {
		return MessageUpdateContainerFluidTank.readFluidTankInfo(buf);
	}

	/**
	 * Write a data instance to the buffer.
	 *
	 * @param buf           The buffer
	 * @param fluidTankInfo The data instance
	 */
	@Override
	protected void writeCapabilityData(final ByteBuf buf, final FluidTankInfo fluidTankInfo) {
		MessageUpdateContainerFluidTank.writeFluidTankInfo(buf, fluidTankInfo);
	}

	public static class Handler extends MessageBulkUpdateContainerCapability.Handler<IFluidHandlerItem, FluidTankInfo, MessageBulkUpdateContainerFluidTanks> {

		/**
		 * Apply the capability data from the data instance to the capability handler instance.
		 *
		 * @param fluidHandlerItem The capability handler instance
		 * @param fluidTankInfo    The data instance
		 */
		@Override
		protected void applyCapabilityData(final IFluidHandlerItem fluidHandlerItem, final FluidTankInfo fluidTankInfo) {
			if (fluidHandlerItem instanceof FluidTank) {
				((FluidTank) fluidHandlerItem).setFluid(fluidTankInfo.fluid);
			}
		}
	}
}
