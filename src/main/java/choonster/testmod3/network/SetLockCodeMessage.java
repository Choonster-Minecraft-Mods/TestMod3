package choonster.testmod3.network;

import choonster.testmod3.capability.lock.LockCapability;
import choonster.testmod3.client.gui.LockScreen;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.LockCode;
import net.minecraftforge.event.network.CustomPayloadEvent;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * Sent to the server by {@link LockScreen} to set the new lock code.
 *
 * @author Choonster
 */
public record SetLockCodeMessage(BlockPos pos, Optional<Direction> direction, String lockCode) {
	public static final StreamCodec<RegistryFriendlyByteBuf, SetLockCodeMessage> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			SetLockCodeMessage::pos,
			ByteBufCodecs.optional(Direction.STREAM_CODEC),
			SetLockCodeMessage::direction,
			ByteBufCodecs.STRING_UTF8,
			SetLockCodeMessage::lockCode,
			SetLockCodeMessage::new
	);

	public SetLockCodeMessage(final BlockPos pos, @Nullable final Direction direction, final String lockCode) {
		this(pos, Optional.ofNullable(direction), lockCode);
	}

	public static void handle(final SetLockCodeMessage message, final CustomPayloadEvent.Context ctx) {
		final var player = Objects.requireNonNull(ctx.getSender());
		final var level = player.level();

		player.resetLastActionTime();

		if (level.isAreaLoaded(message.pos, 1)) {
			LockCapability.getLock(level, message.pos, message.direction.orElse(null)).ifPresent((lock) -> {
				if (lock.isLocked()) {
					player.sendSystemMessage(Component.translatable(TestMod3Lang.LOCK_ALREADY_LOCKED.getTranslationKey()));
				}

				lock.setLockCode(new LockCode(message.lockCode));
			});
		}
	}
}
