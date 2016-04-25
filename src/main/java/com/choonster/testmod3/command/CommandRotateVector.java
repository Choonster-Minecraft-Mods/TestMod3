package com.choonster.testmod3.command;

import com.choonster.testmod3.util.VectorUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentTranslation;

import javax.vecmath.Matrix3d;
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
		return "rotatevector";
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
	 * Callback for when the command is executed
	 *
	 * @param server The Minecraft server instance
	 * @param sender The source of the command invocation
	 * @param args   The arguments that were passed
	 */
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 5) throw new WrongUsageException("commands.testmod3.rotateVector.usage");

		final double x = parseDouble(args[0]), y = parseDouble(args[1]), z = parseDouble(args[2]);
		final Vector3d inputVector = new Vector3d(x, y, z);

		final EnumFacing.Axis axis = EnumFacing.Axis.byName(args[3].toLowerCase(Locale.ENGLISH));
		if (axis == null) throw new WrongUsageException("commands.testmod3.rotateVector.invalidAxis");

		final int degrees = parseInt(args[4]);

		final Matrix3d rotationMatrix = VectorUtils.getRotationMatrix(axis, Math.toRadians(degrees));

		rotationMatrix.transform(inputVector);

		sender.addChatMessage(new TextComponentTranslation("commands.testmod3.rotateVector.result", inputVector.getX(), inputVector.getY(), inputVector.getZ()));
	}
}
