package choonster.testmod3.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

/**
 * A simple implementation of {@link ICapabilityProvider} and {@link INBTSerializable} that supports a single {@link Capability} handler instance.
 * <p>
 * Uses the {@link Capability}'s {@link IStorage} to serialise/deserialise NBT.
 *
 * @author Choonster
 */
public class SerializableCapabilityProvider<HANDLER> extends SimpleCapabilityProvider<HANDLER> implements INBTSerializable<INBT> {

	/**
	 * Create a provider for the default handler instance.
	 *
	 * @param capability The Capability instance to provide the handler for
	 * @param facing     The Direction to provide the handler for
	 */
	public SerializableCapabilityProvider(final Capability<HANDLER> capability, @Nullable final Direction facing) {
		this(capability, facing, capability.getDefaultInstance());
	}

	/**
	 * Create a provider for the specified handler instance.
	 *
	 * @param capability The Capability instance to provide the handler for
	 * @param facing     The Direction to provide the handler for
	 * @param instance   The handler instance to provide
	 */
	public SerializableCapabilityProvider(final Capability<HANDLER> capability, @Nullable final Direction facing, @Nullable final HANDLER instance) {
		super(capability, facing, instance);
	}

	@Nullable
	@Override
	public INBT serializeNBT() {
		final HANDLER instance = getInstance();

		if (instance == null) {
			return null;
		}

		return getCapability().writeNBT(instance, getFacing());
	}

	@Override
	public void deserializeNBT(final INBT nbt) {
		final HANDLER instance = getInstance();

		if (instance == null) {
			return;
		}

		getCapability().readNBT(instance, getFacing(), nbt);
	}

}
