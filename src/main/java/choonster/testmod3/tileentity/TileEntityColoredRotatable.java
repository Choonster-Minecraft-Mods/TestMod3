package choonster.testmod3.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * A TileEntity that stores an {@link EnumFacing} value.
 *
 * @author Choonster
 */
public class TileEntityColoredRotatable extends TileEntity {
	private EnumFacing facing = EnumFacing.NORTH;

	public EnumFacing getFacing() {
		return facing;
	}

	public void setFacing(final EnumFacing facing) {
		this.facing = facing;
		markDirty();
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		facing = EnumFacing.getFront(compound.getInteger("facing"));
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("facing", facing.getIndex());
		return compound;
	}

	private void notifyBlockUpdate() {
		final IBlockState state = getWorld().getBlockState(getPos());
		getWorld().notifyBlockUpdate(getPos(), state, state, 3);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		notifyBlockUpdate();
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
	public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		notifyBlockUpdate();
	}

	@Override
	public boolean shouldRefresh(final World world, final BlockPos pos, final IBlockState oldState, final IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}
}
