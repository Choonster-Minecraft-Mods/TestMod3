package choonster.testmod3.client.command;

import net.minecraftforge.client.ClientCommandHandler;

/**
 * Registers this mod's client-side commands.
 *
 * @author Choonster
 */
public class ModCommandsClient {

	public static void registerCommands() {
		final ClientCommandHandler clientCommandHandler = ClientCommandHandler.instance;

		clientCommandHandler.registerCommand(new CommandListPlayers());
	}
}
