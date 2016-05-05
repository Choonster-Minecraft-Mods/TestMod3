package com.choonster.testmod3.command;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;

/**
 * A manager for sub-commands of an {@link ICommand}.
 *
 * @author Choonster
 */
public interface ISubCommandManager extends ICommandManager {
	/**
	 * Register and return a sub-command.
	 *
	 * @param subCommand The sub-command
	 * @return The sub-command
	 */
	ICommand registerSubCommand(ICommand subCommand);
}
