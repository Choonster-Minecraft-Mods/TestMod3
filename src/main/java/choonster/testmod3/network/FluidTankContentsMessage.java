package choonster.testmod3.network;

import choonster.testmod3.client.util.ClientUtil;
import choonster.testmod3.fluid.FluidTankSnapshot;
import choonster.testmod3.world.level.block.FluidTankBlock;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.List;

/**
 * Sent by {@link FluidTankBlock} to notify the player of the tank's contents.
 *
 * @author Choonster
 */
public record FluidTankContentsMessage(List<FluidTankSnapshot> fluidTankSnapshots) {
	public static StreamCodec<RegistryFriendlyByteBuf, FluidTankContentsMessage> STREAM_CODEC = StreamCodec.composite(
			FluidTankSnapshot.LIST_STREAM_CODEC,
			FluidTankContentsMessage::fluidTankSnapshots,
			FluidTankContentsMessage::new
	);
	
	public static void handle(final FluidTankContentsMessage message, final CustomPayloadEvent.Context ctx) {
		final var player = ClientUtil.getClientPlayer();

		if (player == null) {
			return;
		}

		FluidTankBlock.getFluidDataForDisplay(message.fluidTankSnapshots)
				.forEach(player::sendSystemMessage);
	}
}
