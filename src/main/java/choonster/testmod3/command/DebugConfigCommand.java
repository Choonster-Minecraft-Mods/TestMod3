package choonster.testmod3.command;

import choonster.testmod3.config.ModConfig;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TextComponentString;

import java.util.stream.Collectors;

/**
 * Print the contents of {@link ModConfig#exampleMapField} to the chat for debugging purposes.
 *
 * @author Choonster
 */
class DebugConfigCommand {
	static ArgumentBuilder<CommandSource, ?> register() {
		return Commands.literal("debugconfig")
				.executes(ctx -> {
					final String config = ModConfig.exampleMapField.entrySet().stream()
							.map(Object::toString)
							.collect(Collectors.joining(", "));

					ctx.getSource().sendFeedback(new TextComponentString(config), true);
					return 0;
				});
	}
}
