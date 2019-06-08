package choonster.testmod3.util;

import choonster.testmod3.TestMod3;
import choonster.testmod3.client.init.ModGuiFactories;
import choonster.testmod3.network.MessageOpenClientGui;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;

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
	 * The factories are registered in {@link ModGuiFactories}.
	 * <p>
	 * This is similar to {@link NetworkHooks#openGui} for GUIs without a {@link Container}.
	 *
	 * @param player The player to open the GUI for
	 * @param id     The ID of the GUI to open.
	 */
	public static void openClientGui(final EntityPlayerMP player, final ResourceLocation id) {
		openClientGui(player, id, buf -> {
		});
	}

	/**
	 * Requests to open a GUI on the client, from the server
	 * <p>
	 * The factories are registered in {@link ModGuiFactories}.
	 * <p>
	 * This is similar to {@link NetworkHooks#openGui} for GUIs without a {@link Container}.
	 *
	 * @param player The player to open the GUI for
	 * @param id     The ID of the GUI to open.
	 * @param pos    A BlockPos, which will be encoded into the additional data for this request
	 */
	public static void openClientGui(final EntityPlayerMP player, final ResourceLocation id, final BlockPos pos) {
		openClientGui(player, id, buf -> buf.writeBlockPos(pos));
	}

	/**
	 * Requests to open a GUI on the client, from the server
	 * <p>
	 * The factories are registered in {@link ModGuiFactories}.
	 * <p>
	 * This is similar to {@link NetworkHooks#openGui} for GUIs without a {@link Container}.
	 * <p>
	 * The maximum size for {@code extraDataWriter} is 32600 bytes.
	 *
	 * @param player          The player to open the GUI for
	 * @param id              The ID of the GUI to open.
	 * @param extraDataWriter Consumer to write any additional data required by the GUI
	 */
	public static void openClientGui(final EntityPlayerMP player, final ResourceLocation id, final Consumer<PacketBuffer> extraDataWriter) {
		if (player.world.isRemote) return;
		player.closeScreen();
		player.openContainer = player.inventoryContainer;

		final PacketBuffer extraData = new PacketBuffer(Unpooled.buffer());
		extraDataWriter.accept(extraData);
		extraData.readerIndex(0); // Reset to the beginning in case the factories read from it

		final PacketBuffer output = new PacketBuffer(Unpooled.buffer());
		output.writeVarInt(extraData.readableBytes());
		output.writeBytes(extraData);

		if (output.readableBytes() > 32600 || output.readableBytes() < 1) {
			throw new IllegalArgumentException("Invalid PacketBuffer for openClientGui, found " + output.readableBytes() + " bytes");
		}

		final MessageOpenClientGui message = new MessageOpenClientGui(id, extraData);
		TestMod3.network.send(PacketDistributor.PLAYER.with(() -> player), message);
	}

	/**
	 * Writes a nullable {@link EnumFacing} to a {@link PacketBuffer}.
	 *
	 * @param facing The facing
	 * @param buffer The buffer
	 */
	public static void writeNullableFacing(@Nullable final EnumFacing facing, final PacketBuffer buffer) {
		final boolean hasFacing = facing != null;

		buffer.writeBoolean(hasFacing);

		if (hasFacing) {
			buffer.writeEnumValue(facing);
		}
	}

	/**
	 * Reads a nullable {@link EnumFacing} from a {@link PacketBuffer}.
	 *
	 * @param buffer The buffer
	 * @return The facing
	 */
	@Nullable
	public static EnumFacing readNullableFacing(final PacketBuffer buffer) {
		final boolean hasFacing = buffer.readBoolean();

		if (hasFacing) {
			return buffer.readEnumValue(EnumFacing.class);
		}

		return null;
	}
}
