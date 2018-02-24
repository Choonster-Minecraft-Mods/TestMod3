package choonster.testmod3.command;

import choonster.testmod3.config.ModConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.stream.Collectors;

/**
 * Print the contents of {@link ModConfig#exampleMapField} to the chat for debugging purposes.
 *
 * @author Choonster
 */
public class CommandDebugConfig extends CommandBase {
	@Override
	public String getName() {
		return "debugconfig";
	}

	@Override
	public String getUsage(final ICommandSender iCommandSender) {
		return "/debugconfig";
	}

	@Override
	public void execute(final MinecraftServer minecraftServer, final ICommandSender iCommandSender, final String[] strings) throws CommandException {
		final String config = ModConfig.exampleMapField.entrySet().stream().map(Object::toString).collect(Collectors.joining(", "));
		iCommandSender.sendMessage(new TextComponentString(config));
	}
}
