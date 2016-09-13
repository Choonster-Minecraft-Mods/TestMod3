package choonster.testmod3.command;

import choonster.testmod3.init.ModCommands;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A command with sub-commands.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,38153.0.html
 *
 * @author Choonster
 */
public class CommandTestMod3 extends CommandTreeBase {
	public CommandTestMod3() {
		ModCommands.registerSubCommands(this);
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	/**
	 * Gets the name of the command
	 */
	@Override
	public String getCommandName() {
		return "testmod3";
	}

	/**
	 * Gets the usage string for the command.
	 *
	 * @param sender The command sender
	 */
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.testmod3.usage";
	}

	/**
	 * Get a list of commands usable by the command sender, sorted in their natural order.
	 *
	 * @param sender The command sender
	 * @param server The server
	 * @return The possible commands
	 */
	public List<ICommand> getSortedPossibleCommands(ICommandSender sender, MinecraftServer server) {
		final List<ICommand> commands = getSubCommands().stream()
				.filter(command -> command.checkPermission(server, sender))
				.collect(Collectors.toList());

		Collections.sort(commands);

		return commands;
	}
}
