package choonster.testmod3.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * An reusable implementation of {@link INameable}.
 *
 * @author Choonster
 */
public class NameHolder implements INameable, INBTSerializable<CompoundNBT> {
	/**
	 * The default name.
	 */
	private final ITextComponent defaultName;

	/**
	 * The custom name, if any.
	 */
	private ITextComponent customName;

	public NameHolder(final ITextComponent defaultName) {
		this.defaultName = defaultName.copy();
	}

	@Override
	public ITextComponent getName() {
		return hasCustomName() ? customName : defaultName;
	}

	@Override
	public boolean hasCustomName() {
		return customName != null;
	}

	@Nullable
	@Override
	public ITextComponent getCustomName() {
		return customName;
	}

	/**
	 * Set the custom name of this object.
	 *
	 * @param customName The custom name
	 */
	public void setCustomName(final ITextComponent customName) {
		this.customName = customName.copy();
	}

	@Override
	public CompoundNBT serializeNBT() {
		final CompoundNBT nbt = new CompoundNBT();

		if (hasCustomName()) {
			nbt.putString("DisplayName", ITextComponent.Serializer.toJson(getDisplayName()));
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(final CompoundNBT nbt) {
		if (nbt.contains("DisplayName")) {
			final ITextComponent customName = Objects.requireNonNull(ITextComponent.Serializer.fromJson(nbt.getString("DisplayName")));
			setCustomName(customName);
		}
	}
}
