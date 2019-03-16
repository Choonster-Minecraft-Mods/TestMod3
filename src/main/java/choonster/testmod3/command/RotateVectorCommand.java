package choonster.testmod3.command;

import choonster.testmod3.command.arguments.AxisArgument;
import choonster.testmod3.util.VectorUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

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

	private static int execute(final CommandContext<CommandSource> context, final Vec3d inputVector, final EnumFacing.Axis axis, final int degrees) {
		final Matrix3d rotationMatrix = VectorUtils.getRotationMatrix(axis, Math.toRadians(degrees));

		final Vector3d outputVector = new Vector3d(inputVector.x, inputVector.y, inputVector.z);
		rotationMatrix.transform(outputVector);

		context.getSource().sendFeedback(new TextComponentTranslation("commands.testmod3.rotatevector.result", outputVector.getX(), outputVector.getY(), outputVector.getZ()), true);

		return 0;
	}
}
