package choonster.testmod3.command;

import choonster.testmod3.command.arguments.AxisArgument;
import choonster.testmod3.util.VectorUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;


/**
 * A command that rotates a vector around the specified axis by the specified amount.
 *
 * @author Choonster
 */
public class RotateVectorCommand {
	public static ArgumentBuilder<CommandSource, ?> register() {
		return Commands.literal("rotatevector")
				.then(Commands.argument("vector", Vec3Argument.vec3())
						.then(Commands.argument("axis", AxisArgument.axis())
								.then(Commands.argument("degrees", IntegerArgumentType.integer()))
								.executes(context ->
										execute(
												context,
												Vec3Argument.getVec3(context, "vector"),
												AxisArgument.getAxis(context, "axis"),
												IntegerArgumentType.getInteger(context, "degrees")
										)
								)
						)
				);
	}

	private static int execute(final CommandContext<CommandSource> context, final Vector3d inputVector, final Direction.Axis axis, final int degrees) {
		final Quaternion rotationQuaternion = VectorUtils.getRotationQuaternion(axis, (float) Math.toRadians(degrees));

		final Vector3f outputVector = new Vector3f((float) inputVector.x, (float) inputVector.y, (float) inputVector.z);
		outputVector.transform(rotationQuaternion);

		context.getSource().sendFeedback(new TranslationTextComponent("commands.testmod3.rotatevector.result", outputVector.getX(), outputVector.getY(), outputVector.getZ()), true);

		return 0;
	}
}
