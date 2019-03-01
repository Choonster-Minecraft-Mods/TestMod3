package choonster.testmod3.event;

import choonster.testmod3.TestMod3;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handler for {@code FMLNetworkEvent} subclasses.
 * <p>
 * WARNING: These events are fired on a Netty thread rather than the main client/server thread, so the handler methods must not directly interact with normal Minecraft classes.
 * <p>
 * Use {@link IThreadListener#addScheduledTask} to run a task on the main thread where you can safely interact with normal Minecraft classes.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TestMod3.MODID)
public class NetworkEventHandler {
	private static final Logger LOGGER = LogManager.getLogger();


	/**
	 * Prints server connection info when the client connects to a server.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforge.net/forum/index.php/topic,34546.0.html
	 *
	 * @param event The event
	 */

	/*
	// TODO: Uncomment when event is re-added (https://github.com/MinecraftForge/MinecraftForge/issues/5536)
	@SubscribeEvent
	public static void clientConnectedToServer(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
		final IThreadListener mainThread = Minecraft.getMinecraft();
		mainThread.addScheduledTask(() -> {
			final ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
			LOGGER.info("Server Connected! Local? {} - Address: {}", event.isLocal(), serverData != null ? serverData.serverIP : "<No ServerData>");
		});
	}
	*/
}
