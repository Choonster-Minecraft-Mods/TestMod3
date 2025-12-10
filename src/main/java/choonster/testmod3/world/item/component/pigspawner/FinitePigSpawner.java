package choonster.testmod3.world.item.component.pigspawner;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.DebugUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

/**
 * A spawner that can only spawn a finite number of pigs.
 *
 * @param numPigs    The current number of pigs that can be spawned.
 * @param maxNumPigs The maximum number of pigs that can be spawned.
 * @author Choonster
 */
public record FinitePigSpawner(int numPigs, int maxNumPigs) implements IPigSpawnerFinite {
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final MapCodec<FinitePigSpawner> CODEC = RecordCodecBuilder.mapCodec(builder ->
			builder.group(
					Codec.INT
							.fieldOf("num_pigs")
							.forGetter(FinitePigSpawner::numPigs),

					Codec.INT
							.fieldOf("max_num_pigs")
							.forGetter(FinitePigSpawner::maxNumPigs)
			).apply(builder, FinitePigSpawner::new)
	);

	public static final StreamCodec<ByteBuf, FinitePigSpawner> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT,
			FinitePigSpawner::numPigs,
			ByteBufCodecs.VAR_INT,
			FinitePigSpawner::maxNumPigs,
			FinitePigSpawner::new
	);

	public static FinitePigSpawner empty(final int maxNumPigs) {
		return new FinitePigSpawner(0, maxNumPigs);
	}

	public FinitePigSpawner(final int numPigs, final int maxNumPigs) {
		Preconditions.checkArgument(numPigs <= maxNumPigs,
				"Attempted to set numPigs to %s, but maximum is %s",
				numPigs,
				maxNumPigs()
		);

		this.numPigs = numPigs;
		this.maxNumPigs = maxNumPigs;

		LOGGER.debug(PigSpawner.LOG_MARKER, "Creating finite pig spawner: {}", this, DebugUtil.getStackTrace(10));
	}

	@Override
	public PigSpawnerType getType() {
		return PigSpawnerType.FINITE;
	}

	@Override
	public FinitePigSpawner withNumPigs(final int numPigs) {
		return new FinitePigSpawner(numPigs, maxNumPigs);
	}

	@Override
	public boolean canSpawnPig(final Level level, final double x, final double y, final double z) {
		return numPigs() > 0;
	}

	@Nullable
	@Override
	public IPigSpawnerFinite spawnPig(final Level level, final double x, final double y, final double z) {
		final var success = IPigSpawnerFinite.super.spawnPig(level, x, y, z) != null;

		if (!success) {
			return null;
		}

		return withNumPigs(numPigs() - 1);
	}

	@Override
	public List<MutableComponent> getTooltipLines() {
		return ImmutableList.of(Component.translatable(TestMod3Lang.PIG_SPAWNER_FINITE_DESC.getTranslationKey(), numPigs(), maxNumPigs()));
	}
}
