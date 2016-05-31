package com.choonster.testmod3.command;

import com.choonster.testmod3.init.ModCommands;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A command with sub-commands.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,38153.0.html
 *
 * @author Choonster
 */
public class CommandTestMod3 extends CommandBase {
	/**
	 * The {@link ISubCommandManager} that manages the sub-commands of this command.
	 */
	private final SubCommandHandler subCommandHandler;

	public CommandTestMod3(MinecraftServer server) {
		subCommandHandler = new SubCommandHandler(server);
	}

	/**
	 * Join the arguments array into a single string.
	 *
	 * @param args The arguments
	 * @return The joined string
	 */
	private static String joinArgs(String[] args) {
		return String.join(" ", (CharSequence[]) args);
	}

	/**
	 * Return a copy of the array with the first string removed.
	 * <p>
	 * Copied from {@link CommandHandler#dropFirstString}.
	 *
	 * @param input The original array
	 * @return The new array
	 */
	private static String[] dropFirstString(String[] input) {
		final String[] output = new String[input.length - 1];
		System.arraycopy(input, 1, output, 0, input.length - 1);
		return output;
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
	 * Callback for when the command is executed
	 *
	 * @param server The Minecraft server instance
	 * @param sender The source of the command invocation
	 * @param args   The arguments that were passed
	 */
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		subCommandHandler.executeCommand(sender, joinArgs(args));
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		return subCommandHandler.getTabCompletionOptions(sender, joinArgs(args), pos);
	}

	/**
	 * Return whether the specified command parameter index is a username parameter.
	 */
	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		final ICommand subCommand = subCommandHandler.getCommand(args[0]);
		return index > 0 && subCommand != null && subCommand.isUsernameIndex(dropFirstString(args), index - 1);
	}

	/**
	 * Handler for the sub-commands of this command.
	 */
	private static class SubCommandHandler extends CommandHandler implements ISubCommandManager {
		/**
		 * A map of command names/aliases to commands in the order they were registered.
		 */
		private final Map<String, ICommand> orderedCommandMap = new LinkedHashMap<>();

		/**
		 * A set of the registered commands in the order they were registered.
		 */
		private final Set<ICommand> orderedCommandSet = new LinkedHashSet<>();

		/**
		 * The server.
		 */
		private final MinecraftServer server;

		private SubCommandHandler(MinecraftServer server) {
			this.server = server;
			ModCommands.registerSubCommands(this);
		}

		@Override
		protected MinecraftServer getServer() {
			return server;
		}

		/**
		 * Get the command with the specified name.
		 *
		 * @param commandName The command name
		 * @return The command, or null if there isn't one
		 */
		@Nullable
		public ICommand getCommand(String commandName) {
			return getCommands().get(commandName);
		}

		@Override
		public ICommand registerCommand(ICommand command) {
			orderedCommandMap.put(command.getCommandName(), command);
			orderedCommandSet.add(command);

			for (String alias : command.getCommandAliases()) {
				ICommand iCommand = orderedCommandMap.get(alias);

				if (iCommand == null || !iCommand.getCommandName().equals(alias)) {
					orderedCommandMap.put(alias, command);
				}
			}

			return super.registerCommand(command);
		}

		@Override
		public List<String> getTabCompletionOptions(ICommandSender sender, String input, @Nullable BlockPos pos) {
			final String[] args = input.split(" ", -1);
			final String commandName = args[0];

			if (args.length == 1) {
				final List<String> list = new ArrayList<>();

				list.addAll(this.orderedCommandMap.entrySet().stream()
						.filter(entry -> CommandBase.doesStringStartWith(commandName, entry.getKey()))
						.filter(entry -> entry.getValue().checkPermission(this.getServer(), sender))
						.map(Map.Entry::getKey)
						.collect(Collectors.toList()));

				return list;
			} else {
				if (args.length > 1) {
					final ICommand iCommand = this.orderedCommandMap.get(commandName);

					if (iCommand != null && iCommand.checkPermission(this.getServer(), sender)) {
						return iCommand.getTabCompletionOptions(this.getServer(), sender, dropFirstString(args), pos);
					}
				}

				return Collections.emptyList();
			}
		}

		@Override
		public List<ICommand> getPossibleCommands(ICommandSender sender) {
			final List<ICommand> list = new ArrayList<>();

			list.addAll(orderedCommandSet.stream()
					.filter(iCommand -> iCommand.checkPermission(this.getServer(), sender))
					.collect(Collectors.toList()));

			return list;
		}

		/**
		 * Register and return a sub-command.
		 *
		 * @param subCommand The sub-command
		 * @return The sub-command
		 */
		@Override
		public ICommand registerSubCommand(ICommand subCommand) {
			return registerCommand(subCommand);
		}
	}
}
