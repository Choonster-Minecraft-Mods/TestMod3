package choonster.testmod3.client.event;

import choonster.testmod3.TestMod3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.Connection;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handler for {@link ClientPlayerNetworkEvent} subclasses.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TestMod3.MODID)
public class ClientPlayerNetworkEventHandler {
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Prints server connection info when the client connects to a server.
	 * <p>
	 * Test for this thread:
	 * https://www.minecraftforge.net/forum/topic/34334-get-current-server-address/
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void clientConnectedToServer(final ClientPlayerNetworkEvent.LoggedInEvent event) {
		final ServerData serverData = Minecraft.getInstance().getCurrentServer();
		final Connection networkManager = event.getNetworkManager();

		LOGGER.info("Server Connected! Local? {} - Address: {}", networkManager != null ? String.valueOf(networkManager.isMemoryConnection()) : "<No NetworkManager>", serverData != null ? serverData.ip : "<No ServerData>");
	}
}
