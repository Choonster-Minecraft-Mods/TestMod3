package choonster.testmod3.world.item.component.pigspawner;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A pig spawner. Unless otherwise noted, all methods are only called on the server.
 *
 * @author Choonster
 */
public interface IPigSpawner {
	Codec<IPigSpawner> CODEC = PigSpawnerType.CODEC.dispatch(IPigSpawner::getType, PigSpawnerType::getCodec);

	StreamCodec<ByteBuf, IPigSpawner> STREAM_CODEC = PigSpawnerType.STREAM_CODEC.dispatch(
			IPigSpawner::getType,
			PigSpawnerType::getStreamCodec
	);

	/**
	 * Get the type of this spawner.
	 *
	 * @return The type.
	 */
	PigSpawnerType getType();

	/**
	 * Can a pig be spawned at the specified position?
	 *
	 * @param level The level
	 * @param x     The x coordinate
	 * @param y     The y coordinate
	 * @param z     The z coordinate
	 * @return Can a pig be spawned?
	 */
	boolean canSpawnPig(final Level level, final double x, final double y, final double z);

	/**
	 * Spawn a pig at the specified position.
	 *
	 * @param level The level
	 * @param x     The x coordinate
	 * @param y     The y coordinate
	 * @param z     The z coordinate
	 * @return The new pig spawner state if a pig was successfully spawned, otherwise, null.
	 */
	@Nullable
	default IPigSpawner spawnPig(final Level level, final double x, final double y, final double z) {
		final var pig = EntityType.PIG.create(level, EntitySpawnReason.SPAWN_ITEM_USE);

		if (pig == null) {
			return null;
		}

		pig.setPos(x, y, z);

		final var success = level.addFreshEntity(pig);
		return success ? this : null;
	}

	/**
	 * Get the tooltip lines for this spawner. Can be called on the client or server.
	 *
	 * @return The tooltip lines
	 */
	List<MutableComponent> getTooltipLines();

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
