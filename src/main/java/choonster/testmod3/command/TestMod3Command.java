package choonster.testmod3.command;

import choonster.testmod3.command.maxhealth.MaxHealthCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

/**
 * A command with sub-commands.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,38153.0.html
 *
 * @author Choonster
 */
public class TestMod3Command {
	public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("testmod3")
						.then(RotateVectorCommand.register())
						.then(MaxHealthCommand.register())
						.then(RunTestsCommand.register())
		);
	}
}
