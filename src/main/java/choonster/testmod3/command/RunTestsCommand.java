package choonster.testmod3.command;

import choonster.testmod3.tests.Tests;
import choonster.testmod3.text.TestMod3Lang;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * A command that runs this mod's tests.
 *
 * @author Choonster
 */
class RunTestsCommand {
	static ArgumentBuilder<CommandSourceStack, ?> register() {
		return Commands.literal("runtests")
				.executes(context -> {
					if (Tests.runTests()) {
						context.getSource().sendSuccess(new TranslatableComponent(TestMod3Lang.COMMAND_RUN_TESTS_TESTS_PASSED.getTranslationKey()), true);
					} else {
						context.getSource().sendSuccess(new TranslatableComponent(TestMod3Lang.COMMAND_RUN_TESTS_TESTS_FAILED.getTranslationKey()), true);
					}

					return 0;
				});
	}
}
