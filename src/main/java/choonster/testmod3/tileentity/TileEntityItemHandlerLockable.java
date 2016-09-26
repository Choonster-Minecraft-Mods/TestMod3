package choonster.testmod3.tileentity;

import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.CapabilityLock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * A {@link TileEntity} with a single {@link IItemHandler} inventory, locked with an {@link ILock}.
 *
 * @author Choonster
 */
public abstract class TileEntityItemHandlerLockable<INVENTORY extends IItemHandler & INBTSerializable<NBTTagCompound>, LOCK extends ILock & INBTSerializable<NBTTagCompound>> extends TileEntityItemHandler<INVENTORY> {

	/**
	 * The lock.
	 */
	protected final LOCK lock = createLock();

	/**
	 * Create and return the lock.
	 *
	 * @return The lock
	 */
	protected abstract LOCK createLock();

	@Override
	public void openGUI(World world, EntityPlayer player) {
		if (lock.tryOpen(player)) {
			super.openGUI(world, player);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		lock.deserializeNBT(compound.getCompoundTag("Lock"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("Lock", lock.serializeNBT());
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		final NBTTagCompound updateTag = super.getUpdateTag();
		updateTag.setTag("Lock", lock.serializeNBT());
		return updateTag;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityLock.LOCK_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityLock.LOCK_CAPABILITY) {
			return CapabilityLock.LOCK_CAPABILITY.cast(lock);
		}

		return super.getCapability(capability, facing);
	}
}
