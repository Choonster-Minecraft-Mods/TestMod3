package choonster.testmod3.command;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;

/**
 * Add max health to an entity using {@link IMaxHealth}.
 *
 * @author Choonster
 */
public class CommandMaxHealthAdd extends CommandMaxHealthBase {
	/**
	 * Make a change to the entity's {@link IMaxHealth}.
	 *
	 * @param entity    The entity
	 * @param maxHealth The entity's IMaxHealth
	 * @param amount    The amount to add/set
	 */
	@Override
	protected void processEntity(EntityLivingBase entity, IMaxHealth maxHealth, float amount) {
		maxHealth.addBonusMaxHealth(amount);
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
		return "message.testmod3:max_health.add";
	}

	/**
	 * Gets the name of the command
	 */
	@Override
	public String getName() {
		return "add_max_health";
	}

	/**
	 * Gets the usage string for the command.
	 *
	 * @param sender The command sender
	 */
	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.testmod3.add_max_health.usage";
	}
}
