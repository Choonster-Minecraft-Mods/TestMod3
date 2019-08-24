package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.command.TestMod3Command;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

/**
 * Registers this mod's commands.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID)
public class ModCommands {
	/**
	 * Register the commands and command arguments.
	 *
	 * @param event The server starting event
	 */
	@SubscribeEvent
	public static void registerCommands(final FMLServerStartingEvent event) {
		TestMod3Command.register(event.getCommandDispatcher());
	}
}
