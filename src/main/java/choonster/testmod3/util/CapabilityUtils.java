package choonster.testmod3.util;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

/**
 * Utility methods for Capabilities.
 *
 * @author Choonster
 */
public class CapabilityUtils {
	/**
	 * Get a capability handler from an {@link ICapabilityProvider} if it exists.
	 *
	 * @param provider   The provider
	 * @param capability The capability
	 * @return A lazy optional containing the handler, if any.
	 */
	public static <T> LazyOptional<T> getCapability(@Nullable final ICapabilityProvider provider, final Capability<T> capability) {
		return provider != null ? provider.getCapability(capability) : LazyOptional.empty();
	}

	/**
	 * Get a capability handler from an {@link ICapabilityProvider} if it exists.
	 *
	 * @param provider   The provider
	 * @param capability The capability
	 * @param facing     The facing
	 * @param <T>        The handler type
	 * @return A lazy optional containing the handler, if any.
	 */
	public static <T> LazyOptional<T> getCapability(@Nullable final ICapabilityProvider provider, final Capability<T> capability, @Nullable final EnumFacing facing) {
		return provider != null ? provider.getCapability(capability, facing) : LazyOptional.empty();
	}
}
