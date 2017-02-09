package choonster.testmod3.network.capability.fluidhandler;

import choonster.testmod3.network.capability.MessageUpdateContainerCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;

/**
 * Updates the {@link FluidTank} for a single slot of a {@link Container}.
 *
 * @author Choonster
 */
public class MessageUpdateContainerFluidTank extends MessageUpdateContainerCapability<IFluidHandlerItem, FluidTankInfo> {

	@SuppressWarnings("unused")
	public MessageUpdateContainerFluidTank() {
		super(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
	}

	public MessageUpdateContainerFluidTank(final int windowID, final int slotNumber, final IFluidHandlerItem fluidHandlerItem) {
		super(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null, windowID, slotNumber, fluidHandlerItem);
	}

	/**
	 * Convert the capability handler instance to a data instance.
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
	 * Read the capability data from the buffer.
	 *
	 * @param buf The buffer
	 * @return The data instance
	 */
	@Override
	protected FluidTankInfo readCapabilityData(final ByteBuf buf) {
		return readFluidTankInfo(buf);
	}

	/**
	 * Write the capability data to the buffer.
	 *
	 * @param buf           The buffer
	 * @param fluidTankInfo The data instance
	 */
	@Override
	protected void writeCapabilityData(final ByteBuf buf, final FluidTankInfo fluidTankInfo) {
		writeFluidTankInfo(buf, fluidTankInfo);
	}

	/**
	 * Read a {@link FluidTankInfo} from the buffer
	 *
	 * @param buf The buffer
	 * @return The FluidTankInfo
	 */
	static FluidTankInfo readFluidTankInfo(final ByteBuf buf) {
		final NBTTagCompound tagCompound = ByteBufUtils.readTag(buf);
		final FluidStack contents = FluidStack.loadFluidStackFromNBT(tagCompound);

		final int capacity = buf.readInt();

		return new FluidTankInfo(contents, capacity);
	}

	/**
	 * Write a {@link FluidTankInfo} to the buffer
	 *
	 * @param buf           The buffer
	 * @param fluidTankInfo The FluidTankInfo
	 */
	static void writeFluidTankInfo(final ByteBuf buf, final FluidTankInfo fluidTankInfo) {
		final FluidStack contents = fluidTankInfo.fluid;
		final NBTTagCompound tagCompound = new NBTTagCompound();

		if (contents != null) {
			contents.writeToNBT(tagCompound);
		}

		ByteBufUtils.writeTag(buf, tagCompound);

		buf.writeInt(fluidTankInfo.capacity);
	}

	public static class Handler extends MessageUpdateContainerCapability.Handler<IFluidHandlerItem, FluidTankInfo, MessageUpdateContainerFluidTank> {

		/**
		 * Apply the capability data from the data instance to the capability handler instance.
		 *
		 * @param fluidHandlerItem The capability handler instance
		 * @param fluidTankInfo    The data
		 */
		@Override
		protected void applyCapabilityData(final IFluidHandlerItem fluidHandlerItem, final FluidTankInfo fluidTankInfo) {
			if (fluidHandlerItem instanceof FluidTank) {
				((FluidTank) fluidHandlerItem).setFluid(fluidTankInfo.fluid);
			}
		}
	}
}
