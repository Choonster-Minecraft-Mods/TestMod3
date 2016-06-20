package choonster.testmod3.command;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.CapabilityMaxHealth;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;

/**
 * Get the current max health of an entity and the bonus max health provided by its {@link IMaxHealth}.
 *
 * @author Choonster
 */
public class CommandMaxHealthGet extends CommandMaxHealthBase {
	/**
	 * Make a change to the entity's {@link IMaxHealth}.
	 *
	 * @param entity    The entity
	 * @param maxHealth The entity's IMaxHealth
	 * @param amount    The amount to add/set
	 */
	@Override
	protected void processEntity(EntityLivingBase entity, IMaxHealth maxHealth, float amount) {
		// No-op, this command doesn't make any changes and needs to display a custom message.
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
		if (args.length < 1) {
			throw new WrongUsageException(getCommandUsage(sender));
		}

		final EntityLivingBase entity = getEntity(server, sender, args[0], EntityLivingBase.class);
		final IMaxHealth maxHealth = CapabilityMaxHealth.getMaxHealth(entity);

		notifyCommandListener(sender, this, getSuccessMessage(), entity.getDisplayName(), CapabilityMaxHealth.formatMaxHealth(entity.getMaxHealth()), CapabilityMaxHealth.formatMaxHealth(maxHealth.getBonusMaxHealth()));
	}

	/**
	 * Get the translation key of the message to send when the command succeeds.
	 * <p>
	 * This will be provided with the entity's display name and the amount as format arguments.
	 *
	 * @return The success message's translation key
	 */
	@Override
	protected String getSuccessMessage() {
		return "message.testmod3:maxHealth.get";
	}

	/**
	 * Gets the name of the command
	 */
	@Override
	public String getCommandName() {
		return "getMaxHealth";
	}

	/**
	 * Gets the usage string for the command.
	 *
	 * @param sender The command sender
	 */
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.testmod3.getMaxHealth.usage";
	}
}
