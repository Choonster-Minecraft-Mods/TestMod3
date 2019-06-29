package choonster.testmod3.network;

import choonster.testmod3.capability.lock.LockCapability;
import choonster.testmod3.client.gui.LockSecreen;
import choonster.testmod3.util.NetworkUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Sent to the server by {@link LockSecreen} to set the new lock code.
 *
 * @author Choonster
 */
public class SetLockCodeMessage {
	private final BlockPos pos;
	private final Direction facing;
	private final String lockCode;

	public SetLockCodeMessage(final BlockPos pos, @Nullable final Direction facing, final String lockCode) {
		this.pos = pos;
		this.facing = facing;
		this.lockCode = lockCode;
	}

	public static SetLockCodeMessage decode(final PacketBuffer buffer) {
		final BlockPos pos = BlockPos.fromLong(buffer.readLong());
		final Direction facing = NetworkUtil.readNullableFacing(buffer);
		final String lockCode = buffer.readString(Short.MAX_VALUE);

		return new SetLockCodeMessage(pos, facing, lockCode);
	}

	public static void encode(final SetLockCodeMessage message, final PacketBuffer buffer) {
		buffer.writeLong(message.pos.toLong());
		NetworkUtil.writeNullableFacing(message.facing, buffer);
		buffer.writeString(message.lockCode);
	}


	public static void handle(final SetLockCodeMessage message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final ServerPlayerEntity player = ctx.get().getSender();
			final World world = player.world;

			player.markPlayerActive();

			if (world.isAreaLoaded(message.pos, 1)) {
				LockCapability.getLock(world, message.pos, message.facing).ifPresent((lock) -> {
					if (lock.isLocked()) {
						player.sendMessage(new TranslationTextComponent("testmod3.lock.already_locked"));
					}

					lock.setLockCode(new LockCode(message.lockCode));
				});
			}
		});

		ctx.get().setPacketHandled(true);
	}

}
