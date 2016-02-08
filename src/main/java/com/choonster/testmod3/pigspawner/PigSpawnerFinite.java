package com.choonster.testmod3.pigspawner;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.api.pigspawner.IPigSpawnerFinite;
import com.choonster.testmod3.util.DebugUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import java.util.List;

/**
 * A spawner that can only spawn a finite number of pigs.
 *
 * @author Choonster
 */
public class PigSpawnerFinite extends PigSpawnerBase implements IPigSpawnerFinite {
	/**
	 * The current number of pigs that can be spawned.
	 */
	private int numPigs;

	/**
	 * The maximum number of pigs that can be spawned.
	 */
	private final int maxNumPigs;

	public PigSpawnerFinite(int maxNumPigs) {
		this.maxNumPigs = maxNumPigs;
		Logger.info(CapabilityPigSpawner.LOG_MARKER, DebugUtil.getStackTrace(10), "Creating finite pig spawner: %s", this);
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
	public void setNumPigs(int numPigs) {
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
	public boolean canSpawnPig(World world, double x, double y, double z) {
		return getNumPigs() > 0;
	}

	/**
	 * Spawn a pig at the specified position.
	 * <p>
	 * This method is responsible for decreasing the pig count.
	 *
	 * @param world The world
	 * @param x     The x coordinate
	 * @param y     The y coordinate
	 * @param z     The z coordinate
	 * @return Was the pig successfully spawned?
	 */
	@Override
	public boolean spawnPig(World world, double x, double y, double z) {
		setNumPigs(getNumPigs() - 1);
		return super.spawnPig(world, x, y, z);
	}

	/**
	 * Add tooltip lines for this spawner. Can be called on the client or server.
	 */
	@Override
	public List<IChatComponent> getTooltipLines() {
		return ImmutableList.of(new ChatComponentTranslation("testmod3:pigSpawner.finite.desc", getNumPigs(), getMaxNumPigs()));
	}
}
