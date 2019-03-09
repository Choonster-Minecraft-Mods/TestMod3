package choonster.testmod3.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.capability.TileFluidHandler;

import javax.annotation.Nullable;

/**
 * A base class for this mod's fluid tank {@link TileEntity}s.
 */
public abstract class TileEntityFluidTankBase extends TileFluidHandler {
	public TileEntityFluidTankBase(final TileEntityType<?> tileEntityType) {
		super(tileEntityType);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return write(new NBTTagCompound());
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
		read(pkt.getNbtCompound());
	}
}
