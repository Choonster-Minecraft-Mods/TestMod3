package choonster.testmod3.network;

import choonster.testmod3.client.util.ClientUtil;
import choonster.testmod3.fluid.FluidTankSnapshot;
import choonster.testmod3.world.level.block.FluidTankBlock;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.List;

/**
 * Sent by {@link FluidTankBlock} to notify the player of the tank's contents.
 *
 * @author Choonster
 */
public class FluidTankContentsMessage {
	private final List<FluidTankSnapshot> fluidTankSnapshots;

	public FluidTankContentsMessage(final List<FluidTankSnapshot> fluidTankSnapshots) {
		this.fluidTankSnapshots = fluidTankSnapshots;
	}

	public static FluidTankContentsMessage decode(final FriendlyByteBuf buffer) {
		// TODO: This assumes buffer is always a RegistryFriendlyByteBuf
		final var fluidTankSnapshots = FluidTankSnapshot.LIST_STREAM_CODEC.decode((RegistryFriendlyByteBuf) buffer);

		return new FluidTankContentsMessage(fluidTankSnapshots);
	}

	public static void encode(final FluidTankContentsMessage message, final FriendlyByteBuf buffer) {
		FluidTankSnapshot.LIST_STREAM_CODEC.encode((RegistryFriendlyByteBuf) buffer, message.fluidTankSnapshots);
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
