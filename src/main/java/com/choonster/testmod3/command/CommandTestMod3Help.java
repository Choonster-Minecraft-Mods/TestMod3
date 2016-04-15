package com.choonster.testmod3.command;

import net.minecraft.command.CommandHelp;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Shows help for the {@code /testmod3} command.
 *
 * @author Choonster
 */
public class CommandTestMod3Help extends CommandHelp {
	/**
	 * The {@link ISubCommandManager} this is registered with.
	 */
	private final ISubCommandManager subCommandManager;

	public CommandTestMod3Help(ISubCommandManager subCommandManager) {
		this.subCommandManager = subCommandManager;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.testmod3:help.usage";
	}

	@Override
	protected Map<String, ICommand> getCommandMap(MinecraftServer server) {
		return subCommandManager.getCommands();
	}

	@Override
	protected List<ICommand> getSortedPossibleCommands(ICommandSender sender, MinecraftServer server) {
		List<ICommand> commands = subCommandManager.getPossibleCommands(sender);
		Collections.sort(commands);
		return commands;
	}
}
