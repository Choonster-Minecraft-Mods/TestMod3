package com.choonster.testmod3.command;

import com.choonster.testmod3.tests.Tests;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

/**
 * A command that runs this mod's tests.
 *
 * @author Choonster
 */
public class CommandRunTests extends CommandBase {
	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	/**
	 * Gets the name of the command.
	 */
	@Override
	public String getCommandName() {
		return "runtests";
	}

	/**
	 * Gets the usage string for the command.
	 *
	 * @param sender The command sender that executed the command
	 */
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.testmod3.runTests.usage";
	}

	/**
	 * Callback when the command is invoked
	 *
	 * @param sender The command sender that executed the command
	 * @param args   The arguments that were passed
	 */
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (Tests.runTests()) {
			sender.addChatMessage(new ChatComponentTranslation("commands.testmod3.runTests.testsPassed"));
		} else {
			sender.addChatMessage(new ChatComponentTranslation("commands.testmod3.runTests.testsFailed"));
		}
	}
}