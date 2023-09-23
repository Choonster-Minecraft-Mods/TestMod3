package choonster.testmod3.network;

import choonster.testmod3.client.gui.ClientScreenManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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
public class OpenClientScreenMessage {
	private final ResourceLocation id;
	private final FriendlyByteBuf additionalData;

	public OpenClientScreenMessage(final ResourceLocation id, final FriendlyByteBuf additionalData) {
		this.id = id;
		this.additionalData = additionalData;
	}

	public static OpenClientScreenMessage decode(final FriendlyByteBuf buffer) {
		final var id = buffer.readResourceLocation();
		final var additionalData = new FriendlyByteBuf(Unpooled.wrappedBuffer(buffer.readByteArray(32600)));

		return new OpenClientScreenMessage(id, additionalData);
	}

	public static void encode(final OpenClientScreenMessage message, final FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(message.id);
		buffer.writeByteArray(message.additionalData.readByteArray());
	}

	public static void handle(final OpenClientScreenMessage message, final CustomPayloadEvent.Context ctx) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientScreenManager.openScreen(message.id, message.additionalData));
	}
}
