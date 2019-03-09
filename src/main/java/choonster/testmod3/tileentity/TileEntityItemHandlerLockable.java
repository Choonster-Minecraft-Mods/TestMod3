package choonster.testmod3.tileentity;

import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.CapabilityLock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * A {@link TileEntity} with a single {@link IItemHandler} inventory, locked with an {@link ILock}.
 *
 * @author Choonster
 */
public abstract class TileEntityItemHandlerLockable<
		INVENTORY extends IItemHandler & INBTSerializable<NBTTagCompound>,
		LOCK extends ILock & INBTSerializable<NBTTagCompound>
		> extends TileEntityItemHandler<INVENTORY> {
	/**
	 * The lock.
	 */
	protected final LOCK lock = createLock();

	private final LazyOptional<LOCK> holder = LazyOptional.of(() -> lock);

	public TileEntityItemHandlerLockable(final TileEntityType<?> tileEntityType) {
		super(tileEntityType);
	}

	/**
	 * Create and return the lock.
	 *
	 * @return The lock
	 */
	protected abstract LOCK createLock();

	@Override
	public void openGUI(final World world, final EntityPlayer player) {
		if (lock.tryOpen(player)) {
			super.openGUI(world, player);
		}
	}

	@Override
	public void read(final NBTTagCompound compound) {
		super.read(compound);
		lock.deserializeNBT(compound.getCompound("Lock"));
	}

	@Override
	public NBTTagCompound write(final NBTTagCompound compound) {
		super.write(compound);
		compound.put("Lock", lock.serializeNBT());
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		final NBTTagCompound updateTag = super.getUpdateTag();
		updateTag.put("Lock", lock.serializeNBT());
		return updateTag;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}

	@Override
	protected void invalidateCaps() {
		super.invalidateCaps();
		holder.invalidate();
	}

	@Override
	public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
		if (capability == CapabilityLock.LOCK_CAPABILITY) {
			return holder.cast();
		}

		return super.getCapability(capability, facing);
	}
}
