package choonster.testmod3.client.command;

import choonster.testmod3.TestMod3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Registers this mod's client-side commands.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ModCommandsClient {
	@SubscribeEvent
	public static void registerCommands(final FMLClientSetupEvent event) {
		final ClientCommandHandler clientCommandHandler = ClientCommandHandler.instance;

		clientCommandHandler.registerCommand(new CommandListPlayers());
	}
}
