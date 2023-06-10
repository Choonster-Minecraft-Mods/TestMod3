package choonster.testmod3.command;

import choonster.testmod3.command.arguments.AxisArgument;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.VectorUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 * A command that rotates a vector around the specified axis by the specified amount.
 *
 * @author Choonster
 */
public class RotateVectorCommand {
	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		return Commands.literal("rotatevector")
				.then(Commands.argument("vector", Vec3Argument.vec3())
						.then(Commands.argument("axis", AxisArgument.axis())
								.then(Commands.argument("degrees", IntegerArgumentType.integer())
										.executes(context ->
												execute(
														context,
														Vec3Argument.getVec3(context, "vector"),
														AxisArgument.getAxis(context, "axis"),
														IntegerArgumentType.getInteger(context, "degrees")
												)
										)
								)
						)
				);
	}

	private static int execute(final CommandContext<CommandSourceStack> context, final Vec3 inputVector, final Direction.Axis axis, final int degrees) {
		final var rotationQuaternion = VectorUtils.getRotationQuaternion(axis, (float) Math.toRadians(degrees));

		final var outputVector = new Vector3f((float) inputVector.x, (float) inputVector.y, (float) inputVector.z);
		outputVector.rotate(rotationQuaternion);

		context.getSource().sendSuccess(() -> Component.translatable(TestMod3Lang.COMMAND_ROTATE_VECTOR_RESULT.getTranslationKey(), outputVector.x(), outputVector.y(), outputVector.z()), true);

		return 0;
	}
}
