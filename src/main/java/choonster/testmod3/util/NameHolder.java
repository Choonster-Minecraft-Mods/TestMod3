package choonster.testmod3.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A reusable implementation of {@link Nameable}.
 *
 * @author Choonster
 */
public class NameHolder implements Nameable {
	/**
	 * The default name.
	 */
	private final Component defaultName;

	/**
	 * The custom name, if any.
	 */
	@Nullable
	private Component customName;

	public NameHolder(final Component defaultName) {
		this.defaultName = defaultName.copy();
	}

	@Override
	public Component getName() {
		return customName != null ? customName : defaultName;
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

	public CompoundTag save(final CompoundTag tag, final HolderLookup.Provider registries) {
		if (hasCustomName()) {
			tag.putString("DisplayName", Component.Serializer.toJson(getDisplayName(), registries));
		}

		return tag;
	}

	public void load(final CompoundTag tag, final HolderLookup.Provider registries) {
		if (tag.contains("DisplayName")) {
			final var customName = Objects.requireNonNull(Component.Serializer.fromJson(tag.getString("DisplayName"), registries));
			setCustomName(customName);
		}
	}
}
