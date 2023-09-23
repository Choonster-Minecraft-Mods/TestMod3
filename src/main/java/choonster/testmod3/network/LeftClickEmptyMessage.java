package choonster.testmod3.network;

import choonster.testmod3.world.item.ILeftClickEmpty;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.Objects;

/**
 * Sent to the server when a player left-clicks empty space with an {@link ILeftClickEmpty} item.
 */
public class LeftClickEmptyMessage {
	@SuppressWarnings("InstantiationOfUtilityClass")
	public static LeftClickEmptyMessage decode(final FriendlyByteBuf buffer) {
		return new LeftClickEmptyMessage();
	}

	public static void encode(final LeftClickEmptyMessage message, final FriendlyByteBuf buffer) {
		// No-op
	}

	public static void handle(final LeftClickEmptyMessage message, final CustomPayloadEvent.Context ctx) {
		final var player = Objects.requireNonNull(ctx.getSender());

		final var mainHand = player.getMainHandItem();
		if (!(mainHand.getItem() instanceof final ILeftClickEmpty leftClickEmpty)) {
			return;
		}

		leftClickEmpty.onLeftClickEmpty(mainHand, player);
	}
}
