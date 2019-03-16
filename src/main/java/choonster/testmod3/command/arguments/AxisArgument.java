package choonster.testmod3.command.arguments;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Collection;

/**
 * A command argument for a single {@link EnumFacing.Axis} value.
 *
 * @author Choonster
 */
public class AxisArgument implements ArgumentType<EnumFacing.Axis> {
	private static final Collection<String> EXAMPLES = ImmutableList.of("x", "y", "z");
	private static final SimpleCommandExceptionType INVALID_AXIS_EXCEPTION = new SimpleCommandExceptionType(new TextComponentTranslation("arguments.testmod3.axis.invalid"));

	public static AxisArgument axis() {
		return new AxisArgument();
	}

	public static EnumFacing.Axis getAxis(final CommandContext<CommandSource> context, final String name) {
		return context.getArgument(name, EnumFacing.Axis.class);
	}

	public EnumFacing.Axis parse(final StringReader reader) throws CommandSyntaxException {
		final EnumFacing.Axis axis = EnumFacing.Axis.byName(reader.readString());

		if (axis == null) {
			throw INVALID_AXIS_EXCEPTION.create();
		}

		return axis;
	}

	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
