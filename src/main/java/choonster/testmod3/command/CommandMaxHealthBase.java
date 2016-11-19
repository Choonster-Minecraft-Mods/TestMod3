package choonster.testmod3.command;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.CapabilityMaxHealth;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Base class for commands that affect an entity's {@link IMaxHealth}.
 *
 * @author Choonster
 */
public abstract class CommandMaxHealthBase extends CommandBase {
	/**
	 * Make a change to the entity's {@link IMaxHealth}.
	 *
	 * @param entity    The entity
	 * @param maxHealth The entity's IMaxHealth
	 * @param amount    The amount to add/set
	 */
	protected abstract void processEntity(EntityLivingBase entity, IMaxHealth maxHealth, float amount);

	/**
	 * Get the translation key of the message to send when the command succeeds.
	 * <p>
	 * This will be provided with the entity's display name and the amount as format arguments.
	 *
	 * @return The success message's translation key
	 */
	protected abstract String getSuccessMessage();

	/**
	 * Callback for when the command is executed
	 *
	 * @param server The Minecraft server instance
	 * @param sender The source of the command invocation
	 * @param args   The arguments that were passed
	 */
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException(getUsage(sender));
		}

		final EntityLivingBase entity = getEntity(server, sender, args[0], EntityLivingBase.class);
		final float amount = (float) parseDouble(args[1], -Float.MAX_VALUE, Float.MAX_VALUE);

		final IMaxHealth maxHealth = CapabilityMaxHealth.getMaxHealth(entity);
		if (maxHealth != null) {
			processEntity(entity, maxHealth, amount);
		}

		notifyCommandListener(sender, this, getSuccessMessage(), entity.getDisplayName(), CapabilityMaxHealth.formatMaxHealth(amount));
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
	}
}
