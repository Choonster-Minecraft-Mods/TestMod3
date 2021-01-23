package choonster.testmod3.client.network;

import choonster.testmod3.client.gui.ClientScreenManager;
import choonster.testmod3.network.OpenClientScreenMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.DistExecutor;

/**
 * Client-only methods used by various network message handlers.
 *
 * @author Choonster
 */
public class ClientOnlyNetworkMethods {
	public static DistExecutor.SafeRunnable openClientScreen(final OpenClientScreenMessage message) {
		return new DistExecutor.SafeRunnable() {
			@Override
			public void run() {
				ClientScreenManager.openScreen(message.getId(), message.getAdditionalData(), Minecraft.getInstance());
			}
		};
	}
}
