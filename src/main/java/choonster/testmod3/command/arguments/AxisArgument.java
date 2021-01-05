package choonster.testmod3.command.arguments;

import choonster.testmod3.text.TestMod3Lang;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

/**
 * A command argument for a single {@link Direction.Axis} value.
 *
 * @author Choonster
 */
public class AxisArgument implements ArgumentType<Direction.Axis> {
	private static final Collection<String> EXAMPLES = ImmutableList.of("x", "y", "z");
	private static final SimpleCommandExceptionType INVALID_AXIS_EXCEPTION = new SimpleCommandExceptionType(
			new TranslationTextComponent(TestMod3Lang.ARGUMENT_AXIS_INVALID.getTranslationKey())
	);

	public static AxisArgument axis() {
		return new AxisArgument();
	}

	public static Direction.Axis getAxis(final CommandContext<CommandSource> context, final String name) {
		return context.getArgument(name, Direction.Axis.class);
	}

	public Direction.Axis parse(final StringReader reader) throws CommandSyntaxException {
		final Direction.Axis axis = Direction.Axis.byName(reader.readString());

		if (axis == null) {
			throw INVALID_AXIS_EXCEPTION.create();
		}

		return axis;
	}

	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
