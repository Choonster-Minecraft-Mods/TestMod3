package choonster.testmod3.capability.lock;

import choonster.testmod3.api.capability.lock.ILock;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Default implementation of {@link ILock}.
 *
 * @author Choonster
 */
public class Lock implements ILock {
	public static Lock empty(final Nameable nameProvider) {
		return new Lock(nameProvider);
	}

	public static Codec<Lock> codec(final Nameable nameProvider) {
		return RecordCodecBuilder.create(builder -> builder.group(

						LockCode.CODEC
								.fieldOf("code")
								.forGetter(Lock::getLockCode)

				).apply(builder, (code) -> new Lock(code, nameProvider))
		);
	}

	/**
	 * The lock code.
	 */
	@Nonnull
	private LockCode code;

	private final Nameable nameProvider;

	private Lock(final LockCode code, final Nameable nameProvider) {
		this.code = code;
		this.nameProvider = nameProvider;
	}

	private Lock(final Nameable nameProvider) {
		this(LockCode.NO_LOCK, nameProvider);
	}

	@Override
	public boolean isLocked() {
		// An empty ItemStack can only unlock an empty lock code
		return !getLockCode().unlocksWith(ItemStack.EMPTY);
	}

	@Override
	public LockCode getLockCode() {
		return code;
	}

	@Override
	public void setLockCode(final LockCode code) {
		this.code = code;
	}

	@Override
	public Component getDisplayName() {
		return nameProvider.getDisplayName();
	}
}
