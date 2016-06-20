package choonster.testmod3.capability.pigspawner;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

/**
 * A spawner that can spawn an infinite number of pigs.
 *
 * @author Choonster
 */
public class PigSpawnerInfinite extends PigSpawnerBase {
	/**
	 * Can a pig be spawned at the specified position?
	 *
	 * @param world The world
	 * @param x     The x coordinate
	 * @param y     The y coordinate
	 * @param z     The z coordinate
	 * @return Can a pig be spawned?
	 */
	@Override
	public boolean canSpawnPig(World world, double x, double y, double z) {
		return true;
	}

	/**
	 * Add tooltip lines for this spawner. Can be called on the client or server.
	 */
	@Override
	public List<ITextComponent> getTooltipLines() {
		return ImmutableList.of(new TextComponentTranslation("testmod3:pigSpawner.infinite.desc"));
	}
}
