package choonster.testmod3.world.item.component.lastusetime;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Stores the last use time of an item and a flag indicating whether this should be automatically updated.
 *
 * @author Choonster
 */
public record LastUseTimeProperties(long lastUseTime, boolean automaticUpdates) {
	public static Codec<LastUseTimeProperties> CODEC = RecordCodecBuilder.create(builder ->
			builder.group(

					Codec.LONG
							.fieldOf("last_use_time")
							.forGetter(LastUseTimeProperties::lastUseTime),

					Codec.BOOL
							.fieldOf("automatic_updates")
							.forGetter(LastUseTimeProperties::automaticUpdates)

			).apply(builder, LastUseTimeProperties::new)
	);

	public static StreamCodec<RegistryFriendlyByteBuf, LastUseTimeProperties> NETWORK_CODEC = new StreamCodec<>() {
		@Override
		public LastUseTimeProperties decode(final RegistryFriendlyByteBuf buffer) {
			final var lastUseTime = buffer.readVarLong();
			final var automaticUpdates = buffer.readBoolean();

			return new LastUseTimeProperties(lastUseTime, automaticUpdates);
		}

		@Override
		public void encode(final RegistryFriendlyByteBuf buffer, final LastUseTimeProperties value) {
			buffer.writeVarLong(value.lastUseTime);
			buffer.writeBoolean(value.automaticUpdates);
		}
	};

	public static final LastUseTimeProperties DEFAULT = new LastUseTimeProperties(0, false);
}
