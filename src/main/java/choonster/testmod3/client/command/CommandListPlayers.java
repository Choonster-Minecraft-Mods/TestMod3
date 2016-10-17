package choonster.testmod3.client.command;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A client-side command that lists the players currently on the server.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2751363-get-list-of-current-players-in-the-game-from-the
 *
 * @author Choonster
 */
public class CommandListPlayers extends CommandBase {

	private final Minecraft minecraft = Minecraft.getMinecraft();

	/**
	 * Gets the name of the command
	 */
	@Override
	public String getCommandName() {
		return "listplayers";
	}

	/**
	 * Gets the usage string for the command.
	 *
	 * @param sender The command sender
	 */
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.testmod3:listplayers.usage";
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
		final NetHandlerPlayClient netHandlerPlayClient = minecraft.getConnection();

		if (netHandlerPlayClient != null) {
			final Collection<NetworkPlayerInfo> playerInfoMap = netHandlerPlayClient.getPlayerInfoMap();
			final GuiPlayerTabOverlay tabOverlay = minecraft.ingameGUI.getTabList();

			final String outputText = playerInfoMap.stream()
					.map(tabOverlay::getPlayerName)
					.collect(Collectors.joining(", "));

			sender.addChatMessage(new TextComponentTranslation("commands.testmod3:listplayers.players"));
			sender.addChatMessage(new TextComponentString(outputText));
		}
	}
}
