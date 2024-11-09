package choonster.testmod3.world.item.component.pigspawner;

import choonster.testmod3.text.TestMod3Lang;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * A spawner that can spawn an infinite number of pigs.
 *
 * @author Choonster
 */
public record InfinitePigSpawner() implements IPigSpawner {
	public static final InfinitePigSpawner INSTANCE = new InfinitePigSpawner();

	public static final MapCodec<InfinitePigSpawner> CODEC = MapCodec.unit(INSTANCE);

	public static final StreamCodec<ByteBuf, InfinitePigSpawner> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	@Override
	public PigSpawnerType getType() {
		return PigSpawnerType.INFINITE;
	}

	@Override
	public boolean canSpawnPig(final Level level, final double x, final double y, final double z) {
		return true;
	}

	@Override
	public List<MutableComponent> getTooltipLines() {
		return ImmutableList.of(Component.translatable(TestMod3Lang.PIG_SPAWNER_INFINITE_DESC.getTranslationKey()));
	}
}
