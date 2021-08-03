package choonster.testmod3.capability.lock.wrapper;

import choonster.testmod3.api.capability.lock.ILock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

/**
 * {@link ILock} wrapper around {@link BaseContainerBlockEntity}.
 *
 * @author Choonster
 */
public class BaseContainerBlockEntityWrapper implements ILock {
	private static final Field LOCK_KEY = ObfuscationReflectionHelper.findField(BaseContainerBlockEntity.class, /* lockKey */ "f_58621_");

	private final BaseContainerBlockEntity blockEntity;

	public BaseContainerBlockEntityWrapper(final BaseContainerBlockEntity blockEntity) {
		this.blockEntity = blockEntity;
	}

	/**
	 * @return Is this locked?
	 */
	@Override
	public boolean isLocked() {
		// An empty ItemStack can only unlock an empty lock code
		return !getLockCode().unlocksWith(ItemStack.EMPTY);
	}

	/**
	 * Set the lock code.
	 *
	 * @param code The lock code
	 */
	@Override
	public void setLockCode(final LockCode code) {
		try {
			LOCK_KEY.set(blockEntity, code);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(String.format("Couldn't set lock code of LockableBlockEntity at %s", blockEntity.getBlockPos()), e);
		}
	}

	/**
	 * Get the lock code.
	 *
	 * @return The lock code, if any
	 */
	@Override
	public LockCode getLockCode() {
		try {
			return (LockCode) LOCK_KEY.get(blockEntity);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(String.format("Couldn't get lock code of LockableBlockEntity at %s", blockEntity.getBlockPos()), e);
		}
	}

	@Override
	public Component getDisplayName() {
		return blockEntity.getDisplayName();
	}
}
