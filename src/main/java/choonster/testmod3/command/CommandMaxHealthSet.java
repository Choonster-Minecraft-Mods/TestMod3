package choonster.testmod3.command;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;

/**
 * Set the max health provided by an entity's {@link IMaxHealth}.
 *
 * @author Choonster
 */
public class CommandMaxHealthSet extends CommandMaxHealthBase {
	/**
	 * Make a change to the entity's {@link IMaxHealth}.
	 *
	 * @param entity    The entity
	 * @param maxHealth The entity's {@link IMaxHealth}
	 * @param amount    The amount to add/set
	 */
	@Override
	protected void processEntity(EntityLivingBase entity, IMaxHealth maxHealth, float amount) {
		maxHealth.setBonusMaxHealth(amount);
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
		return "message.testmod3:max_health.set";
	}

	/**
	 * Gets the name of the command
	 */
	@Override
	public String getCommandName() {
		return "set_max_health";
	}

	/**
	 * Gets the usage string for the command.
	 *
	 * @param sender The command sender
	 */
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.testmod3.set_max_health.usage";
	}
}
