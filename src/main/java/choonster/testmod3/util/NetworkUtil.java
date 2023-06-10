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
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Utility methods for networking.
 *
 * @author Choonster
 */
public class NetworkUtil {

	/**
	 * Requests to open a screen on the client, from the server
	 * <p>
	 * The factories are registered with {@link ClientScreenManager} in {@link ModScreenFactories}.
	 * <p>
	 * This is similar to {@link NetworkHooks#openScreen} for screens without an {@link AbstractContainerMenu}.
	 *
	 * @param player The player to open the screen for
	 * @param id     The ID of the screen to open.
	 */
	public static void openClientScreen(final ServerPlayer player, final ResourceLocation id) {
		openClientScreen(player, id, buf -> {
		});
	}

	/**
	 * Requests to open a screen on the client, from the server
	 * <p>
	 * The factories are registered with {@link ClientScreenManager} in {@link ModScreenFactories}.
	 * <p>
	 * This is similar to {@link NetworkHooks#openScreen} for screens without an {@link AbstractContainerMenu}.
	 *
	 * @param player The player to open the screen for
	 * @param id     The ID of the screen to open.
	 * @param pos    A BlockPos, which will be encoded into the additional data for this request
	 */
	public static void openClientScreen(final ServerPlayer player, final ResourceLocation id, final BlockPos pos) {
		openClientScreen(player, id, buf -> buf.writeBlockPos(pos));
	}

	/**
	 * Requests to open a screen on the client, from the server
	 * <p>
	 * The factories are registered with {@link ClientScreenManager} in {@link ModScreenFactories}.
	 * <p>
	 * This is similar to {@link NetworkHooks#openScreen} for screens without an {@link AbstractContainerMenu}.
	 * <p>
	 * The maximum size for {@code extraDataWriter} is 32600 bytes.
	 *
	 * @param player          The player to open the screen for
	 * @param id              The ID of the screen to open.
	 * @param extraDataWriter Consumer to write any additional data required by the screen
	 */
	@SuppressWarnings("resource")
	public static void openClientScreen(final ServerPlayer player, final ResourceLocation id, final Consumer<FriendlyByteBuf> extraDataWriter) {
		if (player.level().isClientSide) {
			return;
		}
		
		player.closeContainer();
		player.containerMenu = player.inventoryMenu;

		final var extraData = new FriendlyByteBuf(Unpooled.buffer());
		extraDataWriter.accept(extraData);
		extraData.readerIndex(0); // Reset to the beginning in case the factories read from it

		final var output = new FriendlyByteBuf(Unpooled.buffer());
		output.writeVarInt(extraData.readableBytes());
		output.writeBytes(extraData);

		if (output.readableBytes() > 32600 || output.readableBytes() < 1) {
			throw new IllegalArgumentException("Invalid PacketBuffer for openClientScreen, found " + output.readableBytes() + " bytes");
		}

		final var message = new OpenClientScreenMessage(id, output);
		TestMod3.network.send(PacketDistributor.PLAYER.with(() -> player), message);
	}

	/**
	 * Writes a nullable {@link Direction} to a {@link FriendlyByteBuf}.
	 *
	 * @param facing The facing
	 * @param buffer The buffer
	 */
	public static void writeNullableDirection(@Nullable final Direction facing, final FriendlyByteBuf buffer) {
		final var hasFacing = facing != null;

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
	public static Direction readNullableDirection(final FriendlyByteBuf buffer) {
		final var hasFacing = buffer.readBoolean();

		if (hasFacing) {
			return buffer.readEnum(Direction.class);
		}

		return null;
	}
}
