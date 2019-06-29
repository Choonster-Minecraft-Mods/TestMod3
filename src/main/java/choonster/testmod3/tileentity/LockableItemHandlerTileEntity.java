package choonster.testmod3.tileentity;

import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.LockCapability;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
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
public abstract class LockableItemHandlerTileEntity<
		INVENTORY extends IItemHandler & INBTSerializable<CompoundNBT>,
		LOCK extends ILock & INBTSerializable<CompoundNBT>
		> extends ItemHandlerTileEntity<INVENTORY> {
	/**
	 * The lock.
	 */
	protected final LOCK lock = createLock();

	private final LazyOptional<LOCK> holder = LazyOptional.of(() -> lock);

	public LockableItemHandlerTileEntity(final TileEntityType<?> tileEntityType) {
		super(tileEntityType);
	}

	/**
	 * Create and return the lock.
	 *
	 * @return The lock
	 */
	protected abstract LOCK createLock();

	@Override
	public void openGUI(final ServerPlayerEntity player) {
		if (lock.tryOpen(player)) {
			super.openGUI(player);
		}
	}

	@Override
	public void read(final CompoundNBT compound) {
		super.read(compound);
		lock.deserializeNBT(compound.getCompound("Lock"));
	}

	@Override
	public CompoundNBT write(final CompoundNBT compound) {
		super.write(compound);
		compound.put("Lock", lock.serializeNBT());
		return compound;
	}

	@Override
	public CompoundNBT getUpdateTag() {
		final CompoundNBT updateTag = super.getUpdateTag();
		updateTag.put("Lock", lock.serializeNBT());
		return updateTag;
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(getPos(), 0, getUpdateTag());
	}

	@Override
	protected void invalidateCaps() {
		super.invalidateCaps();
		holder.invalidate();
	}

	@Override
	public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing) {
		if (capability == LockCapability.LOCK_CAPABILITY) {
			return holder.cast();
		}

		return super.getCapability(capability, facing);
	}
}
