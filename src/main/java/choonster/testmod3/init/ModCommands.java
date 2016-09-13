package choonster.testmod3.init;

import choonster.testmod3.command.*;
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
		event.registerServerCommand(new CommandTestMod3());
	}

	/**
	 * Register the sub-commands of the {@code /testmod3} command.
	 *
	 * @param commandTestMod3 The /testmod3 command
	 */
	public static void registerSubCommands(CommandTestMod3 commandTestMod3) {
		commandTestMod3.addSubcommand(new CommandTestMod3Help(commandTestMod3));
		commandTestMod3.addSubcommand(new CommandRotateVector());
		commandTestMod3.addSubcommand(new CommandRunTests());
		commandTestMod3.addSubcommand(new CommandMaxHealthAdd());
		commandTestMod3.addSubcommand(new CommandMaxHealthSet());
		commandTestMod3.addSubcommand(new CommandMaxHealthGet());
	}
}
