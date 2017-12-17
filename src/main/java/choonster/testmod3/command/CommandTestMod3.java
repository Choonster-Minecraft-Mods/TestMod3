package choonster.testmod3.command;

import choonster.testmod3.init.ModCommands;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

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
	public String getName() {
		return "testmod3";
	}

	/**
	 * Gets the usage string for the command.
	 *
	 * @param sender The command sender
	 */
	@Override
	public String getUsage(final ICommandSender sender) {
		return "commands.testmod3.usage";
	}

	/**
	 * Get a list of commands usable by the command sender, sorted in their natural order.
	 *
	 * @param sender The command sender
	 * @param server The server
	 * @return The possible commands
	 */
	public List<ICommand> getSortedPossibleCommands(final ICommandSender sender, final MinecraftServer server) {
		return getSubCommands().stream()
				.filter(command -> command.checkPermission(server, sender))
				.sorted()
				.collect(Collectors.toList());
	}
}
