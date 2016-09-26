package choonster.testmod3.capability.lock;

import choonster.testmod3.api.capability.lock.ILock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.LockCode;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Default implementation of {@link ILock}.
 *
 * @author Choonster
 */
public class Lock implements ILock, INBTSerializable<NBTTagCompound> {
	/**
	 * The lock code.
	 */
	private LockCode code = LockCode.EMPTY_CODE;

	/**
	 * The default name.
	 */
	private final ITextComponent defaultName;

	/**
	 * The custom name, if any.
	 */
	private ITextComponent displayName;

	public Lock(ITextComponent defaultName) {
		this.defaultName = defaultName;
	}

	@Override
	public boolean isLocked() {
		return code != null && !code.isEmpty();
	}

	@Override
	public LockCode getLockCode() {
		return code;
	}

	@Override
	public void setLockCode(LockCode code) {
		this.code = code;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound tagCompound = new NBTTagCompound();

		if (code != null) {
			code.toNBT(tagCompound);
		}

		if (hasCustomName()) {
			tagCompound.setString("DisplayName", ITextComponent.Serializer.componentToJson(getDisplayName()));
		}

		return tagCompound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		code = LockCode.fromNBT(nbt);

		if (nbt.hasKey("DisplayName")) {
			setDisplayName(ITextComponent.Serializer.jsonToComponent(nbt.getString("DisplayName")));
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
	public void setDisplayName(ITextComponent displayName) {
		this.displayName = displayName.createCopy();
	}
}
