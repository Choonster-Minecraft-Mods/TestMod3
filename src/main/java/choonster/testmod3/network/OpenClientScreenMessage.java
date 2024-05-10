package choonster.testmod3.network;

import choonster.testmod3.client.gui.ClientScreenManager;
import choonster.testmod3.client.gui.ClientScreenType;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;

/**
 * Sent by the server to open a client-side {@link Screen}.
 * <p>
 * This is similar to {@link IForgeServerPlayer#openMenu} for GUIs without an {@link AbstractContainerMenu}.
 *
 * @author Choonster
 */
public record OpenClientScreenMessage<DATA>(ClientScreenType<DATA> clientScreenType, DATA extraData) {
	public static StreamCodec<RegistryFriendlyByteBuf, OpenClientScreenMessage<?>> STREAM_CODEC = new StreamCodec<>() {
		@Override
		public OpenClientScreenMessage<?> decode(final RegistryFriendlyByteBuf buf) {
			return decodeInternal(buf);
		}

		@Override
		public void encode(final RegistryFriendlyByteBuf buf, final OpenClientScreenMessage<?> value) {
			encodeInternal(buf, value);
		}

		@SuppressWarnings("unchecked")
		private <T> OpenClientScreenMessage<T> decodeInternal(final RegistryFriendlyByteBuf buf) {
			final var clientScreenType = (ClientScreenType<T>) ClientScreenType.STREAM_CODEC.decode(buf);
			final var extraData = clientScreenType.extraDataCodec().decode(buf);

			return new OpenClientScreenMessage<>(clientScreenType, extraData);
		}

		private <T> void encodeInternal(final RegistryFriendlyByteBuf buf, final OpenClientScreenMessage<T> value) {
			ClientScreenType.STREAM_CODEC.encode(buf, value.clientScreenType);
			value.clientScreenType.extraDataCodec().encode(buf, value.extraData);
		}
	};

	public static void handle(final OpenClientScreenMessage<?> message, final CustomPayloadEvent.Context ctx) {
		handleInternal(message);
	}

	private static <T> void handleInternal(final OpenClientScreenMessage<T> message) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientScreenManager.openScreen(message.clientScreenType, message.extraData));
	}
}
