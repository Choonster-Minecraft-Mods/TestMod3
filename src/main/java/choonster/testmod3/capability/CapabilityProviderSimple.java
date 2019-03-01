package choonster.testmod3.capability;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

/**
 * A simple implementation of {@link ICapabilityProvider} that supports a single {@link Capability} handler instance.
 *
 * @author Choonster
 */
public class CapabilityProviderSimple<HANDLER> implements ICapabilityProvider {

	/**
	 * The {@link Capability} instance to provide the handler for.
	 */
	protected final Capability<HANDLER> capability;

	/**
	 * The {@link EnumFacing} to provide the handler for.
	 */
	protected final EnumFacing facing;

	/**
	 * The handler instance to provide.
	 */
	protected final HANDLER instance;

	/**
	 * A lazy optional containing handler instance to provide.
	 */
	protected final LazyOptional<HANDLER> lazyOptional;

	public CapabilityProviderSimple(final Capability<HANDLER> capability, @Nullable final EnumFacing facing, @Nullable final HANDLER instance) {
		this.capability = capability;
		this.facing = facing;

		this.instance = instance;

		if (this.instance != null) {
			lazyOptional = LazyOptional.of(() -> this.instance);
		} else {
			lazyOptional = LazyOptional.empty();
		}
	}

	/**
	 * Retrieves the handler for the capability requested on the specific side.
	 * The return value CAN be null if the object does not support the capability.
	 * The return value CAN be the same for multiple faces.
	 *
	 * @param capability The capability to check
	 * @param facing     The Side to check from:
	 *                   CAN BE NULL. Null is defined to represent 'internal' or 'self'
	 * @return A lazy optional containing the handler, if this object supports the capability.
	 */
	@Override
	public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
		return getCapability().orEmpty(capability, lazyOptional);
	}

	/**
	 * Get the {@link Capability} instance to provide the handler for.
	 *
	 * @return The Capability instance
	 */
	public final Capability<HANDLER> getCapability() {
		return capability;
	}

	/**
	 * Get the {@link EnumFacing} to provide the handler for.
	 *
	 * @return The EnumFacing to provide the handler for
	 */
	@Nullable
	public EnumFacing getFacing() {
		return facing;
	}

	/**
	 * Get the handler instance.
	 *
	 * @return A lazy optional containing the handler instance
	 */
	@Nullable
	public final HANDLER getInstance() {
		return instance;
	}
}
