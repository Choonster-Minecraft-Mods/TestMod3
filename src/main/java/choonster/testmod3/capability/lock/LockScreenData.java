package choonster.testmod3.capability.lock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public record LockScreenData(BlockPos pos, Optional<Direction> direction) {
	public static StreamCodec<RegistryFriendlyByteBuf, LockScreenData> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			LockScreenData::pos,
			ByteBufCodecs.optional(Direction.STREAM_CODEC),
			LockScreenData::direction,
			LockScreenData::new
	);

	public LockScreenData(final BlockPos pos, @Nullable final Direction direction) {
		this(pos, Optional.ofNullable(direction));
	}
}
