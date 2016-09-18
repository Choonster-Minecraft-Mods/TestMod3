package choonster.testmod3.capability.lockable;

import choonster.testmod3.api.capability.lockable.ILockable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.LockCode;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

/**
 * Default implementation of {@link ILockable}.
 *
 * @author Choonster
 */
public class Lockable implements ILockable, INBTSerializable<NBTTagCompound> {
	private LockCode code = LockCode.EMPTY_CODE;

	@Override
	public boolean isLocked() {
		return code != null && !code.isEmpty();
	}

	@Override
	@Nullable
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

		return tagCompound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		code = LockCode.fromNBT(nbt);
	}
}
