package choonster.testmod3.command.arguments;

import choonster.testmod3.text.TestMod3Lang;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Collection;

/**
 * A command argument for a single {@link Direction.Axis} value.
 *
 * @author Choonster
 */
public class AxisArgument implements ArgumentType<Direction.Axis> {
	private static final Collection<String> EXAMPLES = ImmutableList.of("x", "y", "z");
	private static final SimpleCommandExceptionType INVALID_AXIS_EXCEPTION = new SimpleCommandExceptionType(
			new TranslatableComponent(TestMod3Lang.ARGUMENT_AXIS_INVALID.getTranslationKey())
	);

	public static AxisArgument axis() {
		return new AxisArgument();
	}

	public static Direction.Axis getAxis(final CommandContext<CommandSourceStack> context, final String name) {
		return context.getArgument(name, Direction.Axis.class);
	}

	@Override
	public Direction.Axis parse(final StringReader reader) throws CommandSyntaxException {
		final Direction.Axis axis = Direction.Axis.byName(reader.readString());

		if (axis == null) {
			throw INVALID_AXIS_EXCEPTION.create();
		}

		return axis;
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
