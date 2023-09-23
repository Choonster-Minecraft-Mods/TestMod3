package choonster.testmod3.network;

import choonster.testmod3.client.util.ClientUtil;
import choonster.testmod3.fluid.FluidTankSnapshot;
import choonster.testmod3.world.level.block.FluidTankBlock;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fluids.FluidStack;

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
		final var numTanks = buffer.readInt();
		final var fluidTankSnapshots = new FluidTankSnapshot[numTanks];

		for (var i = 0; i < numTanks; i++) {
			final var contents = FluidStack.readFromPacket(buffer);

			final var capacity = buffer.readInt();

			fluidTankSnapshots[i] = new FluidTankSnapshot(contents, capacity);
		}

		return new FluidTankContentsMessage(fluidTankSnapshots);
	}

	public static void encode(final FluidTankContentsMessage message, final FriendlyByteBuf buffer) {
		buffer.writeInt(message.fluidTankSnapshots.length);

		for (final var snapshot : message.fluidTankSnapshots) {
			final var contents = snapshot.contents();
			contents.writeToPacket(buffer);

			buffer.writeInt(snapshot.capacity());
		}
	}

	public static void handle(final FluidTankContentsMessage message, final CustomPayloadEvent.Context ctx) {
		final var player = ClientUtil.getClientPlayer();

		if (player == null) {
			return;
		}

		FluidTankBlock.getFluidDataForDisplay(message.fluidTankSnapshots)
				.forEach(player::sendSystemMessage);
	}
}
