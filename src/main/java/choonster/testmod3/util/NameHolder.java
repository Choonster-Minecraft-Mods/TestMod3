package choonster.testmod3.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * A reusable implementation of {@link Nameable}.
 *
 * @author Choonster
 */
public class NameHolder implements Nameable, INBTSerializable<CompoundTag> {
	/**
	 * The default name.
	 */
	private final Component defaultName;

	/**
	 * The custom name, if any.
	 */
	private Component customName;

	public NameHolder(final Component defaultName) {
		this.defaultName = defaultName.copy();
	}

	@Override
	public Component getName() {
		return hasCustomName() ? customName : defaultName;
	}

	@Override
	public boolean hasCustomName() {
		return customName != null;
	}

	@Nullable
	@Override
	public Component getCustomName() {
		return customName;
	}

	/**
	 * Set the custom name of this object.
	 *
	 * @param customName The custom name
	 */
	public void setCustomName(final Component customName) {
		this.customName = customName.copy();
	}

	@Override
	public CompoundTag serializeNBT() {
		final CompoundTag nbt = new CompoundTag();

		if (hasCustomName()) {
			nbt.putString("DisplayName", Component.Serializer.toJson(getDisplayName()));
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(final CompoundTag nbt) {
		if (nbt.contains("DisplayName")) {
			final Component customName = Objects.requireNonNull(Component.Serializer.fromJson(nbt.getString("DisplayName")));
			setCustomName(customName);
		}
	}
}
