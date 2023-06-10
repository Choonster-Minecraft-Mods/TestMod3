package choonster.testmod3.network;

import choonster.testmod3.capability.lock.LockCapability;
import choonster.testmod3.client.gui.LockScreen;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.NetworkUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Sent to the server by {@link LockScreen} to set the new lock code.
 *
 * @author Choonster
 */
public class SetLockCodeMessage {
	private final BlockPos pos;
	@Nullable
	private final Direction facing;
	private final String lockCode;

	public SetLockCodeMessage(final BlockPos pos, @Nullable final Direction facing, final String lockCode) {
		this.pos = pos;
		this.facing = facing;
		this.lockCode = lockCode;
	}

	public static SetLockCodeMessage decode(final FriendlyByteBuf buffer) {
		final var pos = BlockPos.of(buffer.readLong());
		final var facing = NetworkUtil.readNullableDirection(buffer);
		final var lockCode = buffer.readUtf(Short.MAX_VALUE);

		return new SetLockCodeMessage(pos, facing, lockCode);
	}

	public static void encode(final SetLockCodeMessage message, final FriendlyByteBuf buffer) {
		buffer.writeLong(message.pos.asLong());
		NetworkUtil.writeNullableDirection(message.facing, buffer);
		buffer.writeUtf(message.lockCode);
	}

	public static void handle(final SetLockCodeMessage message, final Supplier<NetworkEvent.Context> ctx) {
		final var player = ctx.get().getSender();
		final var level = player.level();

		player.resetLastActionTime();

		if (level.isAreaLoaded(message.pos, 1)) {
			LockCapability.getLock(level, message.pos, message.facing).ifPresent((lock) -> {
				if (lock.isLocked()) {
					player.sendSystemMessage(Component.translatable(TestMod3Lang.LOCK_ALREADY_LOCKED.getTranslationKey()));
				}

				lock.setLockCode(new LockCode(message.lockCode));
			});
		}
	}
}
