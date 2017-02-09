package choonster.testmod3.api.capability.pigspawner;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A pig spawner. Unless otherwise noted, all methods are only called on the server.
 *
 * @author Choonster
 */
public interface IPigSpawner {
	/**
	 * Can a pig be spawned at the specified position?
	 *
	 * @param world The world
	 * @param x     The x coordinate
	 * @param y     The y coordinate
	 * @param z     The z coordinate
	 * @return Can a pig be spawned?
	 */
	boolean canSpawnPig(World world, double x, double y, double z);

	/**
	 * Spawn a pig at the specified position.
	 *
	 * @param world The world
	 * @param x     The x coordinate
	 * @param y     The y coordinate
	 * @param z     The z coordinate
	 * @return Was the pig successfully spawned?
	 */
	boolean spawnPig(World world, double x, double y, double z);

	/**
	 * Get the tooltip lines for this spawner. Can be called on the client or server.
	 *
	 * @return The tooltip lines
	 */
	List<ITextComponent> getTooltipLines();

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementations must override {@link Object#equals(Object)} to perform a value comparison instead of a reference
	 * comparison.
	 */
	@Override
	boolean equals(@Nullable final Object obj);

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementations must override {@link Object#hashCode()} to generate a hash code based on the values used in
	 * {@link #equals(Object)}, as per the base method's contract.
	 */
	@Override
	int hashCode();
}
