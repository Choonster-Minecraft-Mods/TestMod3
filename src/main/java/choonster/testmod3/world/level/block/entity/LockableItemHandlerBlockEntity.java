package choonster.testmod3.world.level.block.entity;

import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.LockCapability;
import choonster.testmod3.util.CapabilityNotPresentException;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jspecify.annotations.Nullable;

/**
 * A {@link BlockEntity} with a single {@link IItemHandler} inventory, locked with an {@link ILock}.
 *
 * @author Choonster
 */
public abstract class LockableItemHandlerBlockEntity<INVENTORY extends IItemHandler, LOCK extends ILock>
		extends ItemHandlerBlockEntity<INVENTORY> {
	private final Codec<LOCK> lockCodec = createLockCodec();
	private LazyOptional<LOCK> lockOptional = LazyOptional.of(this::createEmptyLock);

	public LockableItemHandlerBlockEntity(final BlockEntityType<?> blockEntityType, final BlockPos pos, final BlockState state) {
		super(blockEntityType, pos, state);
	}

	/**
	 * Create and return the empty lock.
	 *
	 * @return The lock
	 */
	protected abstract LOCK createEmptyLock();

	/**
	 * Create and return a codec for the lock type.
	 *
	 * @return The lock codec
	 */
	protected abstract Codec<LOCK> createLockCodec();

	private LOCK getLock() {
		return lockOptional.orElseThrow(CapabilityNotPresentException::new);
	}

	@Override
	public void openGUI(final ServerPlayer player) {
		if (getLock().tryOpen(player, getBlockPos().getCenter())) {
			super.openGUI(player);
		}
	}

	@Override
	protected void loadAdditional(final ValueInput input) {
		super.loadAdditional(input);

		input.read("ItemHandler", lockCodec).ifPresent(lock -> {
			lockOptional.invalidate();
			lockOptional = LazyOptional.of(() -> lock);
		});
	}

	@Override
	protected void saveAdditional(final ValueOutput output) {
		super.saveAdditional(output);

		output.store("Lock", lockCodec, getLock());
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		lockOptional.invalidate();
	}

	@Override
	public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing) {
		if (capability == LockCapability.LOCK_CAPABILITY) {
			return lockOptional.cast();
		}

		return super.getCapability(capability, facing);
	}
}
