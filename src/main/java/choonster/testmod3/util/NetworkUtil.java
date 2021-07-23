package choonster.testmod3.util;

import choonster.testmod3.TestMod3;
import choonster.testmod3.client.gui.ClientScreenManager;
import choonster.testmod3.client.init.ModScreenFactories;
import choonster.testmod3.network.OpenClientScreenMessage;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Utility methods for networking.
 *
 * @author Choonster
 */
public class NetworkUtil {

	/**
	 * Requests to open a GUI on the client, from the server
	 * <p>
	 * The factories are registered with {@link ClientScreenManager} in {@link ModScreenFactories}.
	 * <p>
	 * This is similar to {@link NetworkHooks#openGui} for GUIs without an {@link AbstractContainerMenu}.
	 *
	 * @param player The player to open the GUI for
	 * @param id     The ID of the GUI to open.
	 */
	public static void openClientGui(final ServerPlayer player, final ResourceLocation id) {
		openClientGui(player, id, buf -> {
		});
	}

	/**
	 * Requests to open a GUI on the client, from the server
	 * <p>
	 * The factories are registered with {@link ClientScreenManager} in {@link ModScreenFactories}.
	 * <p>
	 * This is similar to {@link NetworkHooks#openGui} for GUIs without an {@link AbstractContainerMenu}.
	 *
	 * @param player The player to open the GUI for
	 * @param id     The ID of the GUI to open.
	 * @param pos    A BlockPos, which will be encoded into the additional data for this request
	 */
	public static void openClientGui(final ServerPlayer player, final ResourceLocation id, final BlockPos pos) {
		openClientGui(player, id, buf -> buf.writeBlockPos(pos));
	}

	/**
	 * Requests to open a GUI on the client, from the server
	 * <p>
	 * The factories are registered with {@link ClientScreenManager} in {@link ModScreenFactories}.
	 * <p>
	 * This is similar to {@link NetworkHooks#openGui} for GUIs without an {@link AbstractContainerMenu}.
	 * <p>
	 * The maximum size for {@code extraDataWriter} is 32600 bytes.
	 *
	 * @param player          The player to open the GUI for
	 * @param id              The ID of the GUI to open.
	 * @param extraDataWriter Consumer to write any additional data required by the GUI
	 */
	public static void openClientGui(final ServerPlayer player, final ResourceLocation id, final Consumer<FriendlyByteBuf> extraDataWriter) {
		if (player.level.isClientSide) return;
		player.closeContainer();
		player.containerMenu = player.inventoryMenu;

		final FriendlyByteBuf extraData = new FriendlyByteBuf(Unpooled.buffer());
		extraDataWriter.accept(extraData);
		extraData.readerIndex(0); // Reset to the beginning in case the factories read from it

		final FriendlyByteBuf output = new FriendlyByteBuf(Unpooled.buffer());
		output.writeVarInt(extraData.readableBytes());
		output.writeBytes(extraData);

		if (output.readableBytes() > 32600 || output.readableBytes() < 1) {
			throw new IllegalArgumentException("Invalid PacketBuffer for openClientGui, found " + output.readableBytes() + " bytes");
		}

		final OpenClientScreenMessage message = new OpenClientScreenMessage(id, output);
		TestMod3.network.send(PacketDistributor.PLAYER.with(() -> player), message);
	}

	/**
	 * Writes a nullable {@link Direction} to a {@link FriendlyByteBuf}.
	 *
	 * @param facing The facing
	 * @param buffer The buffer
	 */
	public static void writeNullableFacing(@Nullable final Direction facing, final FriendlyByteBuf buffer) {
		final boolean hasFacing = facing != null;

		buffer.writeBoolean(hasFacing);

		if (hasFacing) {
			buffer.writeEnum(facing);
		}
	}

	/**
	 * Reads a nullable {@link Direction} from a {@link FriendlyByteBuf}.
	 *
	 * @param buffer The buffer
	 * @return The facing
	 */
	@Nullable
	public static Direction readNullableFacing(final FriendlyByteBuf buffer) {
		final boolean hasFacing = buffer.readBoolean();

		if (hasFacing) {
			return buffer.readEnum(Direction.class);
		}

		return null;
	}
}
