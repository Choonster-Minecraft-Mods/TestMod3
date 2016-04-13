package com.choonster.testmod3.init;

import com.choonster.testmod3.command.*;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Registers this mod's commands.
 *
 * @author Choonster
 */
public class ModCommands {

	/**
	 * Register the commands.
	 *
	 * @param event The server starting event
	 */
	public static void registerCommands(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandRotateVector());
		event.registerServerCommand(new CommandRunTests());
		event.registerServerCommand(new CommandMaxHealthAdd());
		event.registerServerCommand(new CommandMaxHealthSet());
		event.registerServerCommand(new CommandMaxHealthGet());
	}
}
