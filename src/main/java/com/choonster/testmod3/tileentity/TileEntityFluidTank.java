package com.choonster.testmod3.tileentity;

import com.choonster.testmod3.fluids.FluidTankWithTile;
import com.choonster.testmod3.util.FluidUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

/**
 * A tank that holds 10 buckets of fluid.
 * <p>
 * Parts of this code are adapted from EnderIO's {@code TileTank} (https://github.com/SleepyTrousers/EnderIO) and Forge's {@link TileFluidHandler}.
 *
 * @author Choonster, CrazyPants, tterag1098, King Lemming
 */
public class TileEntityFluidTank extends TileEntity implements IFluidHandler {
	private static final int CAPACITY = 10 * FluidContainerRegistry.BUCKET_VOLUME;

	protected FluidTank tank = new FluidTankWithTile(this, CAPACITY);

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
		tank = loadTank(compound, this);
	}

	public void writeTankData(NBTTagCompound compund) {
		saveTank(compund, tank);
	}

	public static FluidTank loadTank(NBTTagCompound tagCompound, TileEntity tileEntity) {
		FluidTank tank = new FluidTankWithTile(tileEntity, CAPACITY);
		tank.readFromNBT(tagCompound);
		return tank;
	}

	public static void saveTank(NBTTagCompound tagCompound, FluidTank tank) {
		tank.writeToNBT(tagCompound);
	}

	/**
	 * Get the remaining capacity in the tank
	 *
	 * @return The remaining capacity
	 */
	public int getRemainingCapacity() {
		return tank.getCapacity() - tank.getFluidAmount();
	}

	/**
	 * Try to fill or drain a fluid container item from/to the tank
	 *
	 * @param container The fluid container item
	 * @param facing    The direction to fill/drain from
	 * @return The new fluid container item, or null if no fill/drain took place
	 */
	public ItemStack tryUseFluidContainer(ItemStack container, EnumFacing facing) {
		if (tank.getFluidAmount() > 0) { // If the tank has fluid in it,
			FluidUtils.FluidResult containerFillResult = FluidUtils.fillFluidContainer(tank.getFluid(), container); // Try to fill the fluid container item
			if (containerFillResult.operationType == FluidUtils.FluidResult.Type.FILL && // If the container was filled,
					canDrain(facing, containerFillResult.fluidConsumed.getFluid()) && // Fluid can be drained from the specified direction and
					FluidUtils.fluidStackHasFluid(drain(facing, containerFillResult.fluidConsumed, false))) { // A simulated drain succeeded,

				drain(facing, containerFillResult.fluidConsumed, true); // Drain the consumed fluid from the tank
				return containerFillResult.newContainer; // Return the filled container
			}
		}

		FluidUtils.FluidResult containerDrainResult = FluidUtils.drainFluidContainer(tank.getFluid(), getRemainingCapacity(), container); // Try to drain the fluid container item
		if (containerDrainResult.operationType == FluidUtils.FluidResult.Type.DRAIN && // If the container was drained,
				canFill(facing, containerDrainResult.fluidConsumed.getFluid()) && // Fluid can be filled from the specified direction and
				fill(facing, containerDrainResult.fluidConsumed, false) > 0) { // A simulated fill succeeded,

			fill(facing, containerDrainResult.fluidConsumed, true);
			return containerDrainResult.newContainer;
		}

		// If no fill/drain took place, return null
		return null;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), tagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
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
