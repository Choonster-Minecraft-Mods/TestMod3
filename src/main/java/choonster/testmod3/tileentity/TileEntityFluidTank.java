package choonster.testmod3.tileentity;

import choonster.testmod3.fluids.FluidTankWithTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.capability.TileFluidHandler;

import javax.annotation.Nullable;

/**
 * A tank that holds 10 buckets of fluid.
 *
 * @author Choonster
 */
public class TileEntityFluidTank extends TileFluidHandler {
	public static final int CAPACITY = 10 * Fluid.BUCKET_VOLUME;

	public TileEntityFluidTank() {
		tank = new FluidTankWithTile(this, CAPACITY);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}
}
