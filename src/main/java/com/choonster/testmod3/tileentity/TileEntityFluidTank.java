package com.choonster.testmod3.tileentity;

import com.choonster.testmod3.fluids.FluidTankWithTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

import javax.annotation.Nullable;

/**
 * A tank that holds 10 buckets of fluid.
 * <p>
 * Parts of this code are adapted from EnderIO's {@code TileTank} (https://github.com/SleepyTrousers/EnderIO) and Forge's {@link TileFluidHandler}.
 *
 * @author Choonster, CrazyPants, tterag1098, King Lemming
 */
public class TileEntityFluidTank extends TileEntity implements IFluidHandler {
	@SuppressWarnings("deprecation")
	private static final int CAPACITY = 10 * FluidContainerRegistry.BUCKET_VOLUME;

	protected FluidTank tank = new FluidTankWithTile(this, CAPACITY);

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		readTankData(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		writeTankData(compound);
		return compound;
	}

	public void readTankData(NBTTagCompound compound) {
		tank = loadTank(compound, this);
	}

	public void writeTankData(NBTTagCompound compund) {
		saveTank(compund, tank);
	}

	public static FluidTank loadTank(NBTTagCompound tagCompound, TileEntity tileEntity) {
		final FluidTank tank = new FluidTankWithTile(tileEntity, CAPACITY);
		tank.readFromNBT(tagCompound);
		return tank;
	}

	public static void saveTank(NBTTagCompound tagCompound, FluidTank tank) {
		tank.writeToNBT(tagCompound);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), writeToNBT(new NBTTagCompound()));
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
