package choonster.testmod3.capability.lock;

import choonster.testmod3.api.capability.lock.ILock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.LockCode;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Default implementation of {@link ILock}.
 *
 * @author Choonster
 */
public class Lock implements ILock, INBTSerializable<CompoundNBT> {
	/**
	 * The lock code.
	 */
	@Nonnull
	private LockCode code = LockCode.EMPTY_CODE;

	/**
	 * The default name.
	 */
	private final ITextComponent defaultName;

	/**
	 * The custom name, if any.
	 */
	private ITextComponent displayName;

	public Lock(final ITextComponent defaultName) {
		this.defaultName = defaultName;
	}

	@Override
	public boolean isLocked() {
		// An empty ItemStack can only unlock an empty lock code
		return !getLockCode().func_219964_a(ItemStack.EMPTY);
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
	public CompoundNBT serializeNBT() {
		final CompoundNBT tagCompound = new CompoundNBT();

		code.write(tagCompound);

		if (hasCustomName()) {
			tagCompound.putString("DisplayName", ITextComponent.Serializer.toJson(getDisplayName()));
		}

		return tagCompound;
	}

	@Override
	public void deserializeNBT(final CompoundNBT nbt) {
		code = LockCode.read(nbt);

		if (nbt.contains("DisplayName")) {
			final ITextComponent displayName = Objects.requireNonNull(ITextComponent.Serializer.fromJson(nbt.getString("DisplayName")));
			setDisplayName(displayName);
		}
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	@Override
	public ITextComponent getName() {
		return getDisplayName();
	}

	/**
	 * Returns true if this thing is named
	 */
	@Override
	public boolean hasCustomName() {
		return displayName != null;
	}

	/**
	 * Get the formatted ChatComponent that will be used for the sender's username in chat
	 */
	@Override
	public ITextComponent getDisplayName() {
		return hasCustomName() ? displayName : defaultName;
	}

	@Nullable
	@Override
	public ITextComponent getCustomName() {
		return displayName;
	}

	/**
	 * Set the display name.
	 *
	 * @param displayName The display name
	 */
	public void setDisplayName(final ITextComponent displayName) {
		this.displayName = displayName.deepCopy();
	}
}
