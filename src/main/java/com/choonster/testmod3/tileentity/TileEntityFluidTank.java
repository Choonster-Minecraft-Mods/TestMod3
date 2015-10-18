package com.choonster.testmod3.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

/**
 * A tank that holds 10 buckets of fluid.
 * <p>
 * Parts of this code are adapted from EnderIO's {@code TileTank} (https://github.com/SleepyTrousers/EnderIO) and Forge's {@link TileFluidHandler}.
 */
public class TileEntityFluidTank extends TileEntity implements IFluidHandler {

	private static final int CAPACITY = 10 * FluidContainerRegistry.BUCKET_VOLUME;

	protected FluidTank tank = new FluidTank(CAPACITY);

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		readTankData(compound);
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		writeTankData(compound);
	}

	public void readTankData(NBTTagCompound compound) {
		tank = loadTank(compound);
	}

	public void writeTankData(NBTTagCompound compund) {
		saveTank(compund, tank);
	}

	public static FluidTank loadTank(NBTTagCompound tagCompound) {
		FluidTank tank = new FluidTank(CAPACITY);
		tank.readFromNBT(tagCompound);
		return tank;
	}

	public static void saveTank(NBTTagCompound tagCompound, FluidTank tank) {
		tank.writeToNBT(tagCompound);
	}

	public ItemStack tryUseFluidContainer(ItemStack container, EnumFacing facing) {
		if (FluidContainerRegistry.isEmptyContainer(container)) {
			FluidStack currentContents = tank.getFluid();
			if (currentContents != null && canDrain(facing, currentContents.getFluid())) {
				int capacity = FluidContainerRegistry.getContainerCapacity(currentContents, container);
				ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(currentContents, container);
				if (filledContainer != null && drain(facing, capacity, false).amount > 0) {
					drain(facing, capacity, true);
					return filledContainer;
				}
			}
		} else if (FluidContainerRegistry.isFilledContainer(container)) {
			FluidStack containerFluidStack = FluidContainerRegistry.getFluidForFilledItem(container);
			if (canFill(facing, containerFluidStack.getFluid())) {
				ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(container);
				if (emptyContainer != null && fill(facing, containerFluidStack, false) == containerFluidStack.amount) {
					fill(facing, containerFluidStack, true);
					return emptyContainer;
				}
			}
		}

		return null;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return new S35PacketUpdateTileEntity(getPos(), getBlockMetadata(), tagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	// IFluidHandler

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		if (resource == null || !resource.isFluidEqual(tank.getFluid())) {
			return null;
		}
		return tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		return new FluidTankInfo[]{tank.getInfo()};
	}
}
