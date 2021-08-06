package choonster.testmod3.world.level.block.entity;

import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.LockCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * A {@link BlockEntity} with a single {@link IItemHandler} inventory, locked with an {@link ILock}.
 *
 * @author Choonster
 */
public abstract class LockableItemHandlerBlockEntity<
		INVENTORY extends IItemHandler & INBTSerializable<CompoundTag>,
		LOCK extends ILock & INBTSerializable<CompoundTag>
		> extends ItemHandlerBlockEntity<INVENTORY> {
	/**
	 * The lock.
	 */
	protected final LOCK lock = createLock();

	private final LazyOptional<LOCK> holder = LazyOptional.of(() -> lock);

	public LockableItemHandlerBlockEntity(final BlockEntityType<?> blockEntityType, final BlockPos pos, final BlockState state) {
		super(blockEntityType, pos, state);
	}

	/**
	 * Create and return the lock.
	 *
	 * @return The lock
	 */
	protected abstract LOCK createLock();

	@Override
	public void openGUI(final ServerPlayer player) {
		if (lock.tryOpen(player)) {
			super.openGUI(player);
		}
	}

	@Override
	public void load(final CompoundTag nbt) {
		super.load(nbt);
		lock.deserializeNBT(nbt.getCompound("Lock"));
	}

	@Override
	public CompoundTag save(final CompoundTag compound) {
		super.save(compound);
		compound.put("Lock", lock.serializeNBT());
		return compound;
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return new ClientboundBlockEntityDataPacket(getBlockPos(), 0, getUpdateTag());
	}

	@Override
	public void invalidateCaps() {
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
