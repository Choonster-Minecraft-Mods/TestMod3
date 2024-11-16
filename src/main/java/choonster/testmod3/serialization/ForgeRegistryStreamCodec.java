package choonster.testmod3.serialization;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

/**
 * A {@link StreamCodec} implementation for Forge registries that don't wrap Vanilla registries.
 * <p>
 * Equivalent to {@link net.minecraft.network.codec.ByteBufCodecs#registry(ResourceKey)}.
 *
 * @author Choonster
 */
@SuppressWarnings("UnstableApiUsage")
public record ForgeRegistryStreamCodec<T>(
		ResourceKey<Registry<T>> registryKey
) implements StreamCodec<FriendlyByteBuf, T> {
	private ForgeRegistry<T> getRegistryOrThrow() {
		final var registry = RegistryManager.ACTIVE.getRegistry(registryKey);

		if (registry == null) {
			throw new IllegalStateException("Missing registry: " + registryKey);
		}

		return registry;
	}

	@Override
	public T decode(final FriendlyByteBuf byteBuf) {
		final var id = byteBuf.readVarInt();
		final var value = getRegistryOrThrow().getValue(id);

		if (value == null) {
			throw new IllegalArgumentException("No value with id " + id);
		}

		return value;
	}

	@Override
	public void encode(final FriendlyByteBuf friendlyByteBuf, final T value) {
		final var i = getRegistryOrThrow().getID(value);

		if (i == -1) {
			throw new IllegalArgumentException("Can't find id for '" + value + "' in registry " + registryKey);
		}

		friendlyByteBuf.writeVarInt(i);
	}
}
