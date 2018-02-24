package choonster.testmod3.init;

import choonster.testmod3.command.*;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.server.command.CommandTreeHelp;

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
	public static void registerCommands(final FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTestMod3());
	}

	/**
	 * Register the sub-commands of the {@code /testmod3} command.
	 *
	 * @param commandTestMod3 The /testmod3 command
	 */
	public static void registerSubCommands(final CommandTestMod3 commandTestMod3) {
		commandTestMod3.addSubcommand(new CommandRotateVector());
		commandTestMod3.addSubcommand(new CommandRunTests());
		commandTestMod3.addSubcommand(new CommandMaxHealthAdd());
		commandTestMod3.addSubcommand(new CommandMaxHealthSet());
		commandTestMod3.addSubcommand(new CommandMaxHealthGet());
		commandTestMod3.addSubcommand(new CommandTreeHelp(commandTestMod3));
		commandTestMod3.addSubcommand(new CommandDebugConfig());
	}
}
