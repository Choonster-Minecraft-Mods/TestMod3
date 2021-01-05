package choonster.testmod3.command;

import choonster.testmod3.tests.Tests;
import choonster.testmod3.text.TestMod3Lang;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * A command that runs this mod's tests.
 *
 * @author Choonster
 */
class RunTestsCommand {
	static ArgumentBuilder<CommandSource, ?> register() {
		return Commands.literal("runtests")
				.executes(context -> {
					if (Tests.runTests()) {
						context.getSource().sendFeedback(new TranslationTextComponent(TestMod3Lang.COMMAND_RUN_TESTS_TESTS_PASSED.getTranslationKey()), true);
					} else {
						context.getSource().sendFeedback(new TranslationTextComponent(TestMod3Lang.COMMAND_RUN_TESTS_TESTS_FAILED.getTranslationKey()), true);
					}

					return 0;
				});
	}
}
