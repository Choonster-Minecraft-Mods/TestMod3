package choonster.testmod3.capability.pigspawner;

import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.util.DebugUtil;
import choonster.testmod3.util.LogUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A spawner that can only spawn a finite number of pigs.
 *
 * @author Choonster
 */
public class PigSpawnerFinite extends PigSpawnerBase implements IPigSpawnerFinite {
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * The current number of pigs that can be spawned.
	 */
	private int numPigs;

	/**
	 * The maximum number of pigs that can be spawned.
	 */
	private final int maxNumPigs;

	public PigSpawnerFinite(final int maxNumPigs) {
		this.maxNumPigs = maxNumPigs;
		LogUtil.debug(LOGGER, CapabilityPigSpawner.LOG_MARKER, DebugUtil.getStackTrace(10), "Creating finite pig spawner: %s", this);
	}

	/**
	 * Get the current number of pigs that can be spawned.
	 *
	 * @return The number of pigs that can be spawned
	 */
	@Override
	public int getNumPigs() {
		return numPigs;
	}

	/**
	 * Get the maximum number of pigs that can be spawned.
	 *
	 * @return The maximum number of pigs that can be spawned.
	 */
	@Override
	public int getMaxNumPigs() {
		return maxNumPigs;
	}

	/**
	 * Set the current number of pigs that can be spawned.
	 *
	 * @param numPigs The number of pigs that can be spawned
	 * @throws IllegalArgumentException If {@code numPigs} is greater than {@link #getMaxNumPigs()}
	 */
	@Override
	public void setNumPigs(final int numPigs) {
		Preconditions.checkArgument(numPigs <= getMaxNumPigs(), "Attempted to set numPigs to %s, but maximum is %s", numPigs, getMaxNumPigs());
		this.numPigs = numPigs;
	}

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
		return getNumPigs() > 0;
	}

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
		setNumPigs(getNumPigs() - 1);
		return super.spawnPig(world, x, y, z);
	}

	/**
	 * Add tooltip lines for this spawner. Can be called on the client or server.
	 */
	@Override
	public List<ITextComponent> getTooltipLines() {
		return ImmutableList.of(new TextComponentTranslation("testmod3.pig_spawner.finite.desc", getNumPigs(), getMaxNumPigs()));
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		final PigSpawnerFinite that = (PigSpawnerFinite) obj;

		return numPigs == that.numPigs && maxNumPigs == that.maxNumPigs;
	}

	@Override
	public int hashCode() {
		int result = numPigs;
		result = 31 * result + maxNumPigs;
		return result;
	}
}
