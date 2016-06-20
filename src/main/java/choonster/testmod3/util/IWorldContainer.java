package choonster.testmod3.util;

import net.minecraft.world.World;

/**
 * An interface to be implemented by things that can contain a {@link World} object.
 *
 * @author Choonster
 */
public interface IWorldContainer {

	/**
	 * Get the {@link World}.
	 *
	 * @return The World
	 */
	World getWorld();
}
