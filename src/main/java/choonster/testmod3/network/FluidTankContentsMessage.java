package choonster.testmod3.network;

import choonster.testmod3.client.util.ClientUtil;
import choonster.testmod3.fluid.FluidTankSnapshot;
import choonster.testmod3.world.level.block.FluidTankBlock;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sent by {@link FluidTankBlock} to notify the player of the tank's contents.
 *
 * @author Choonster
 */
public class FluidTankContentsMessage {
	private final FluidTankSnapshot[] fluidTankSnapshots;

	public FluidTankContentsMessage(final FluidTankSnapshot[] fluidTankSnapshots) {
		this.fluidTankSnapshots = fluidTankSnapshots;
	}

	public static FluidTankContentsMessage decode(final FriendlyByteBuf buffer) {
		final int numTanks = buffer.readInt();
		final FluidTankSnapshot[] fluidTankSnapshots = new FluidTankSnapshot[numTanks];

		for (int i = 0; i < numTanks; i++) {
			final FluidStack contents = FluidStack.readFromPacket(buffer);

			final int capacity = buffer.readInt();

			fluidTankSnapshots[i] = new FluidTankSnapshot(contents, capacity);
		}

		return new FluidTankContentsMessage(fluidTankSnapshots);
	}

	public static void encode(final FluidTankContentsMessage message, final FriendlyByteBuf buffer) {
		buffer.writeInt(message.fluidTankSnapshots.length);

		for (final FluidTankSnapshot snapshot : message.fluidTankSnapshots) {
			final FluidStack contents = snapshot.contents();
			contents.writeToPacket(buffer);

			buffer.writeInt(snapshot.capacity());
		}
	}

	public static void handle(final FluidTankContentsMessage message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final Player player = ClientUtil.getClientPlayer();

			if (player == null) {
				return;
			}

			FluidTankBlock.getFluidDataForDisplay(message.fluidTankSnapshots)
					.forEach((textComponent) -> player.sendMessage(textComponent, Util.NIL_UUID));
		});

		ctx.get().setPacketHandled(true);
	}
}
