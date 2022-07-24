package choonster.testmod3.network;

import choonster.testmod3.world.item.ILeftClickEmpty;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

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

	public static void handle(final LeftClickEmptyMessage message, final Supplier<NetworkEvent.Context> ctx) {
		final var player = Objects.requireNonNull(ctx.get().getSender());

		final var mainHand = player.getMainHandItem();
		if (!(mainHand.getItem() instanceof ILeftClickEmpty leftClickEmpty)) {
			return;
		}

		leftClickEmpty.onLeftClickEmpty(mainHand, player);
	}
}
