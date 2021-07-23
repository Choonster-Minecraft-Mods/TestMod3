package choonster.testmod3.capability.pigspawner;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;

/**
 * Base implementation of {@link IPigSpawner}.
 * <p>
 * {@link #spawnPig} is implemented here instead of as a default method so other implementations can add their own
 * functionality without having to rewrite the pig spawning themselves (you can't call a default method from its override).
 *
 * @author Choonster
 */
public abstract class BasePigSpawner implements IPigSpawner {
	@Override
	public boolean spawnPig(final Level level, final double x, final double y, final double z) {
		final Pig pig = EntityType.PIG.create(level);

		if (pig == null) {
			return false;
		}

		pig.setPos(x, y, z);
		return level.addFreshEntity(pig);
	}
}
