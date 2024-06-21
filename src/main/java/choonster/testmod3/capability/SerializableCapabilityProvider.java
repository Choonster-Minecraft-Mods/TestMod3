package choonster.testmod3.capability;

import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

/**
 * A simple implementation of {@link ICapabilityProvider} and {@link INBTSerializable} that supports a single {@link Capability} handler instance.
 *
 * @author Choonster
 */
public class SerializableCapabilityProvider<HANDLER, IMPLEMENTATION extends HANDLER>
		extends SimpleCapabilityProvider<HANDLER, IMPLEMENTATION> implements INBTSerializable<Tag> {
	private final Codec<IMPLEMENTATION> instanceCodec;

	/**
	 * Create a provider for the specified handler instance.
	 *
	 * @param capability      The Capability instance to provide the handler for
	 * @param facing          The Direction to provide the handler for
	 * @param defaultInstance The default handler instance to provide
	 * @param instanceCodec   The codec for the instance type
	 */
	public SerializableCapabilityProvider(
			final Capability<HANDLER> capability,
			@Nullable final Direction facing,
			final IMPLEMENTATION defaultInstance,
			final Codec<IMPLEMENTATION> instanceCodec
	) {
		super(capability, facing, defaultInstance);
		this.instanceCodec = instanceCodec;
	}

	@Override
	public Tag serializeNBT(final HolderLookup.Provider registries) {
		final var ops = registries.createSerializationContext(NbtOps.INSTANCE);

		return instanceCodec.encodeStart(ops, getInstance()).getOrThrow();
	}

	@Override
	public void deserializeNBT(final HolderLookup.Provider registries, final Tag tag) {
		final var ops = registries.createSerializationContext(NbtOps.INSTANCE);

		final var instance = instanceCodec.parse(ops, tag).getOrThrow();
		replaceInstance(instance);
	}
}
