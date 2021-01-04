package choonster.testmod3.client.network;

import choonster.testmod3.client.init.ModGuiFactories;
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
		return () -> Minecraft.getInstance().displayGuiScreen(ModGuiFactories.getClientScreen(message));
	}
}
