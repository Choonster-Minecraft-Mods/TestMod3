package choonster.testmod3.command;

import choonster.testmod3.command.maxhealth.MaxHealthCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

/**
 * A command with sub-commands.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,38153.0.html
 *
 * @author Choonster
 */
public class TestMod3Command {
	public static void register(final CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
				Commands.literal("testmod3")
						.then(RotateVectorCommand.register())
						.then(DebugConfigCommand.register())
						.then(MaxHealthCommand.register())
						.then(RunTestsCommand.register())
		);
	}
}
