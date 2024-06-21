package choonster.testmod3.capability;

import choonster.testmod3.util.CapabilityNotPresentException;
import com.google.common.base.Preconditions;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

/**
 * A simple implementation of {@link ICapabilityProvider} that supports a single {@link Capability} handler instance.
 *
 * @param <HANDLER>        The capability handler type
 * @param <IMPLEMENTATION> The handler implementation type
 * @author Choonster
 */
public class SimpleCapabilityProvider<HANDLER, IMPLEMENTATION extends HANDLER> implements ICapabilityProvider {
	/**
	 * The {@link Capability} instance to provide the handler for.
	 */
	protected final Capability<HANDLER> capability;

	/**
	 * The {@link Direction} to provide the handler for.
	 */
	@Nullable
	protected final Direction facing;

	/**
	 * A lazy optional containing handler instance to provide.
	 */
	private LazyOptional<IMPLEMENTATION> instanceOptional;

	public SimpleCapabilityProvider(final Capability<HANDLER> capability, @Nullable final Direction facing, final IMPLEMENTATION instance) {
		this.capability = Preconditions.checkNotNull(capability, "capability");
		this.facing = facing;

		instanceOptional = LazyOptional.of(() -> instance);
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
	public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing) {
		return capability == getCapability() ? instanceOptional.cast() : LazyOptional.empty();
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
	 * Get the {@link Direction} to provide the handler for.
	 *
	 * @return The Direction to provide the handler for
	 */
	@Nullable
	public Direction getFacing() {
		return facing;
	}

	/**
	 * Get the handler instance.
	 *
	 * @return A lazy optional containing the handler instance
	 */
	protected final IMPLEMENTATION getInstance() {
		return instanceOptional.orElseThrow(CapabilityNotPresentException::new);
	}

	protected final void replaceInstance(final IMPLEMENTATION newInstance) {
		instanceOptional.invalidate();
		instanceOptional = LazyOptional.of(() -> newInstance);
	}
}
