package choonster.testmod3.network;

import choonster.testmod3.client.init.ModGuiFactories;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

/**
 * Sent by the server to open a client-side {@link GuiScreen}.
 * <p>
 * This is similar to {@link NetworkHooks#openGui} for GUIs without a {@link Container}.
 *
 * @author Choonster
 */
public class MessageOpenClientGui {
	private final ResourceLocation id;
	private final PacketBuffer additionalData;

	public MessageOpenClientGui(final ResourceLocation id, final PacketBuffer additionalData) {
		this.id = id;
		this.additionalData = additionalData;
	}

	public static MessageOpenClientGui decode(final PacketBuffer buffer) {
		final ResourceLocation id = buffer.readResourceLocation();
		final PacketBuffer additionalData = new PacketBuffer(Unpooled.wrappedBuffer(buffer.readByteArray(32600)));

		return new MessageOpenClientGui(id, additionalData);
	}

	public static void encode(final MessageOpenClientGui message, final PacketBuffer buffer) {
		buffer.writeResourceLocation(message.id);
		buffer.writeByteArray(message.additionalData.readByteArray());
	}

	public static void handle(final MessageOpenClientGui message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			Minecraft.getInstance().displayGuiScreen(ModGuiFactories.getClientGui(message));
		}));
		ctx.get().setPacketHandled(true);
	}

	public ResourceLocation getId() {
		return id;
	}

	public PacketBuffer getAdditionalData() {
		return additionalData;
	}
}
