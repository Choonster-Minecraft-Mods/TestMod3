package choonster.testmod3.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.Nameable;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A reusable implementation of {@link Nameable}.
 *
 * @author Choonster
 */
public class NameHolder implements Nameable {
	public static Codec<NameHolder> CODEC = RecordCodecBuilder.create(builder ->
			builder.group(
					ComponentSerialization.CODEC
							.fieldOf("default_name")
							.forGetter((nameHolder) -> nameHolder.defaultName),

					ComponentSerialization.CODEC
							.optionalFieldOf("custom_name")
							.forGetter(nameHolder -> Optional.ofNullable(nameHolder.customName))
			).apply(
					builder,
					(defaultName, customName) ->
							new NameHolder(defaultName, customName.orElse(null))
			)
	);

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

	private NameHolder(final Component defaultName, @Nullable final Component customName) {
		this(defaultName);
		this.customName = customName;
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
}
