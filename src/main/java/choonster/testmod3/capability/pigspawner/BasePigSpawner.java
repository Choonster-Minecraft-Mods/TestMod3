package choonster.testmod3.capability.pigspawner;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.world.World;

/**
 * Base implementation of {@link IPigSpawner}.
 * <p>
 * {@link #spawnPig} is implemented here instead of as a default method so other implementations can add their own
 * functionality without having to rewrite the pig spawning themselves (you can't call a default method from its override).
 *
 * @author Choonster
 */
public abstract class BasePigSpawner implements IPigSpawner {
	/**
	 * Spawn a pig at the specified position.
	 *
	 * @param world The world
	 * @param x     The x coordinate
	 * @param y     The y coordinate
	 * @param z     The z coordinate
	 * @return Was the pig successfully spawned?
	 */
	@Override
	public boolean spawnPig(final World world, final double x, final double y, final double z) {
		final PigEntity pig = EntityType.PIG.create(world);

		if (pig == null) {
			return false;
		}

		pig.setPosition(x, y, z);
		return world.addEntity(pig);
	}
}
