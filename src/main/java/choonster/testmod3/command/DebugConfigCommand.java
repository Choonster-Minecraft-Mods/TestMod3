package choonster.testmod3.command;

import choonster.testmod3.config.TestMod3Config;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TextComponentString;

import java.util.stream.Collectors;

/**
 * Print the contents of {@link TestMod3Config.Common#exampleMapField} to the chat for debugging purposes.
 *
 * @author Choonster
 */
class DebugConfigCommand {
	static ArgumentBuilder<CommandSource, ?> register() {
		return Commands.literal("debugconfig")
				.executes(ctx -> {
					final String config = TestMod3Config.COMMON.exampleMapField.get().entrySet().stream()
							.map(Object::toString)
							.collect(Collectors.joining(", "));

					ctx.getSource().sendFeedback(new TextComponentString(config), true);
					return 0;
				});
	}
}
