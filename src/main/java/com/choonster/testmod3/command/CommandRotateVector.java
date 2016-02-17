package com.choonster.testmod3.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import java.util.Locale;

/**
 * A command that rotates a vector around the specified axis by the specified amount.
 *
 * @author Choonster
 */
public class CommandRotateVector extends CommandBase {

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	/**
	 * Gets the name of the command.
	 */
	@Override
	public String getCommandName() {
		return "rotate-vector";
	}

	/**
	 * Gets the usage string for the command.
	 *
	 * @param sender The command sender that executed the command
	 */
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.testmod3.rotateVector.usage";
	}

	/**
	 * Callback when the command is invoked.
	 *
	 * @param sender The command sender that executed the command
	 * @param args   The arguments that were passed
	 */
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 5) throw new WrongUsageException("commands.testmod3.rotateVector.usage");

		final double x = parseDouble(args[0]), y = parseDouble(args[1]), z = parseDouble(args[2]);
		final Vector3d inputVector = new Vector3d(x, y, z);

		final EnumFacing.Axis axis = EnumFacing.Axis.byName(args[3].toLowerCase(Locale.ENGLISH));
		if (axis == null) throw new WrongUsageException("commands.testmod3.rotateVector.invalidAxis");

		// Get the positive facing for the axis then get its direction vector
		final Vec3i axisVector = EnumFacing.func_181076_a(EnumFacing.AxisDirection.POSITIVE, axis).getDirectionVec();

		final int degrees = parseInt(args[4]);

		final AxisAngle4d axisAngle = new AxisAngle4d(axisVector.getX(), axisVector.getY(), axisVector.getZ(), Math.toRadians(degrees));

		final Matrix3d rotationMatrix = new Matrix3d();
		rotationMatrix.set(axisAngle);

		final Vector3d outputVector = new Vector3d();
		rotationMatrix.transform(inputVector, outputVector);

		sender.addChatMessage(new ChatComponentTranslation("commands.testmod3.rotateVector.result", outputVector.getX(), outputVector.getY(), outputVector.getZ()));
	}
}
