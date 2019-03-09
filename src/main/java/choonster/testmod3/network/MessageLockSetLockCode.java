package choonster.testmod3.network;

import choonster.testmod3.capability.lock.CapabilityLock;
import choonster.testmod3.client.gui.GuiLock;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Sent to the server by {@link GuiLock} to set the new lock code.
 *
 * @author Choonster
 */
public class MessageLockSetLockCode {
	private final BlockPos pos;
	private final boolean hasFacing;
	private final EnumFacing facing;
	private final String lockCode;

	public MessageLockSetLockCode(final BlockPos pos, @Nullable final EnumFacing facing, final String lockCode) {
		this.pos = pos;
		this.facing = facing;
		this.lockCode = lockCode;
		hasFacing = facing != null;
	}

	public static MessageLockSetLockCode decode(final PacketBuffer buffer) {
		final BlockPos pos = BlockPos.fromLong(buffer.readLong());
		final boolean hasFacing = buffer.readBoolean();

		final EnumFacing facing;
		if (hasFacing) {
			facing = buffer.readEnumValue(EnumFacing.class);
		} else {
			facing = null;
		}

		final String lockCode = buffer.readString(Short.MAX_VALUE);

		return new MessageLockSetLockCode(pos, facing, lockCode);
	}

	public static void encode(final MessageLockSetLockCode message, final PacketBuffer buffer) {
		buffer.writeLong(message.pos.toLong());
		buffer.writeBoolean(message.hasFacing);

		if (message.hasFacing) {
			buffer.writeByte(message.facing.getIndex());
		}

		buffer.writeString(message.lockCode);
	}


	public static void handle(final MessageLockSetLockCode message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final EntityPlayerMP player = ctx.get().getSender();
			final World world = player.world;

			player.markPlayerActive();

			if (world.isBlockLoaded(message.pos)) {
				CapabilityLock.getLock(world, message.pos, message.facing).ifPresent((lock) -> {
					if (lock.isLocked()) {
						player.sendMessage(new TextComponentTranslation("testmod3:lock.already_locked"));
					}

					lock.setLockCode(new LockCode(message.lockCode));
				});
			}
		});

		ctx.get().setPacketHandled(true);
	}

}
