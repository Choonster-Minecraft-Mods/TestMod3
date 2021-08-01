package choonster.testmod3.capability.pigspawner;

import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.DebugUtil;
import choonster.testmod3.util.LogUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A spawner that can only spawn a finite number of pigs.
 *
 * @author Choonster
 */
public class FinitePigSpawner extends BasePigSpawner implements IPigSpawnerFinite {
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * The current number of pigs that can be spawned.
	 */
	private int numPigs;

	/**
	 * The maximum number of pigs that can be spawned.
	 */
	private final int maxNumPigs;

	public FinitePigSpawner(final int maxNumPigs) {
		this.maxNumPigs = maxNumPigs;
		LogUtil.debug(LOGGER, PigSpawnerCapability.LOG_MARKER, DebugUtil.getStackTrace(10), "Creating finite pig spawner: {}", this);
	}

	@Override
	public int getNumPigs() {
		return numPigs;
	}

	@Override
	public int getMaxNumPigs() {
		return maxNumPigs;
	}

	@Override
	public void setNumPigs(final int numPigs) {
		Preconditions.checkArgument(numPigs <= getMaxNumPigs(), "Attempted to set numPigs to %s, but maximum is %s", numPigs, getMaxNumPigs());
		this.numPigs = numPigs;
	}

	@Override
	public boolean canSpawnPig(final Level level, final double x, final double y, final double z) {
		return getNumPigs() > 0;
	}

	@Override
	public boolean spawnPig(final Level level, final double x, final double y, final double z) {
		setNumPigs(getNumPigs() - 1);
		return super.spawnPig(level, x, y, z);
	}

	@Override
	public List<MutableComponent> getTooltipLines() {
		return ImmutableList.of(new TranslatableComponent(TestMod3Lang.PIG_SPAWNER_FINITE_DESC.getTranslationKey(), getNumPigs(), getMaxNumPigs()));
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		final FinitePigSpawner that = (FinitePigSpawner) obj;

		return numPigs == that.numPigs && maxNumPigs == that.maxNumPigs;
	}

	@Override
	public int hashCode() {
		int result = numPigs;
		result = 31 * result + maxNumPigs;
		return result;
	}
}
