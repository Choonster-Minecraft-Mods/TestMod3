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
		event.registerServerCommand(new CommandTestMod3(event.getServer()));
	}

	/**
	 * Register the sub-commands of the {@code /testmod3} command.
	 *
	 * @param subCommandManager The sub-command manager of the command
	 */
	public static void registerSubCommands(ISubCommandManager subCommandManager) {
		subCommandManager.registerCommand(new CommandTestMod3Help(subCommandManager));
		subCommandManager.registerCommand(new CommandRotateVector());
		subCommandManager.registerCommand(new CommandRunTests());
		subCommandManager.registerCommand(new CommandMaxHealthAdd());
		subCommandManager.registerCommand(new CommandMaxHealthSet());
		subCommandManager.registerCommand(new CommandMaxHealthGet());
	}
}
