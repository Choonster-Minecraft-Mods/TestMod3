package choonster.testmod3.capability.pigspawner;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

/**
 * A spawner that can spawn an infinite number of pigs.
 *
 * @author Choonster
 */
public class InfinitePigSpawner extends BasePigSpawner {

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
	public boolean canSpawnPig(final World world, final double x, final double y, final double z) {
		return true;
	}

	/**
	 * Add tooltip lines for this spawner. Can be called on the client or server.
	 */
	@Override
	public List<ITextComponent> getTooltipLines() {
		return ImmutableList.of(new TranslationTextComponent("testmod3.pig_spawner.infinite.desc"));
	}
}
