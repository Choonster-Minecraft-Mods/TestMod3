package choonster.testmod3.util;

import choonster.testmod3.TestMod3;
import choonster.testmod3.client.gui.ClientScreenManager;
import choonster.testmod3.client.gui.ClientScreenType;
import choonster.testmod3.client.init.ModScreenConstructors;
import choonster.testmod3.network.OpenClientScreenMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.common.extensions.IForgeServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

/**
 * Utility methods for networking.
 *
 * @author Choonster
 */
public class NetworkUtil {
	/**
	 * Requests to open a screen on the client, from the server
	 * <p>
	 * The factories are registered with {@link ClientScreenManager} in {@link ModScreenConstructors}.
	 * <p>
	 * This is similar to {@link IForgeServerPlayer#openMenu} for screens without an {@link AbstractContainerMenu}.
	 *
	 * @param player           The player to open the screen for
	 * @param clientScreenType The type of the screen to open.
	 * @param extraData        Consumer to write any additional data required by the screen
	 */
	@SuppressWarnings("resource")
	public static <T> void openClientScreen(final ServerPlayer player, final Supplier<ClientScreenType<T>> clientScreenType, final T extraData) {
		if (player.level().isClientSide()) {
			return;
		}

		player.closeContainer();
		player.containerMenu = player.inventoryMenu;

		final var message = new OpenClientScreenMessage<>(clientScreenType.get(), extraData);
		TestMod3.network.send(message, PacketDistributor.PLAYER.with(player));
	}
}
