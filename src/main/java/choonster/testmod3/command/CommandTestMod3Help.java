package choonster.testmod3.command;

import net.minecraft.command.CommandHelp;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.List;
import java.util.Map;

/**
 * Shows help for the {@code /testmod3} command.
 *
 * @author Choonster
 */
public class CommandTestMod3Help extends CommandHelp {
	/**
	 * The {@link CommandTestMod3} instance.
	 */
	private final CommandTestMod3 commandTestMod3;

	public CommandTestMod3Help(CommandTestMod3 commandTestMod3) {
		this.commandTestMod3 = commandTestMod3;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.testmod3:help.usage";
	}

	@Override
	protected Map<String, ICommand> getCommandMap(MinecraftServer server) {
		return commandTestMod3.getCommandMap();
	}

	@Override
	protected List<ICommand> getSortedPossibleCommands(ICommandSender sender, MinecraftServer server) {
		return commandTestMod3.getSortedPossibleCommands(sender, server);
	}
}
