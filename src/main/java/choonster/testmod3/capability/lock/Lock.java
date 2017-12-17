package choonster.testmod3.capability.lock;

import choonster.testmod3.api.capability.lock.ILock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.LockCode;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Default implementation of {@link ILock}.
 *
 * @author Choonster
 */
public class Lock implements ILock, INBTSerializable<NBTTagCompound> {
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
		return !code.isEmpty();
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
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound tagCompound = new NBTTagCompound();

		code.toNBT(tagCompound);

		if (hasCustomName()) {
			tagCompound.setString("DisplayName", ITextComponent.Serializer.componentToJson(getDisplayName()));
		}

		return tagCompound;
	}

	@Override
	public void deserializeNBT(final NBTTagCompound nbt) {
		code = LockCode.fromNBT(nbt);

		if (nbt.hasKey("DisplayName")) {
			final ITextComponent displayName = Objects.requireNonNull(ITextComponent.Serializer.jsonToComponent(nbt.getString("DisplayName")));
			setDisplayName(displayName);
		}
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	@Override
	public String getName() {
		return getDisplayName().getUnformattedText();
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

	/**
	 * Set the display name.
	 *
	 * @param displayName The display name
	 */
	public void setDisplayName(final ITextComponent displayName) {
		this.displayName = displayName.createCopy();
	}
}
