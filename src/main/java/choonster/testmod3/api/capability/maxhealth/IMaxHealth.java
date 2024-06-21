package choonster.testmod3.api.capability.maxhealth;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

/**
 * A capability to provide a max health bonus to an entity.
 *
 * @author Choonster
 */
@AutoRegisterCapability
public interface IMaxHealth {
	/**
	 * Get the bonus max health.
	 *
	 * @return The bonus max health
	 */
	float getBonusMaxHealth();

	/**
	 * Set the bonus max health.
	 *
	 * @param bonusMaxHealth The bonus max health
	 */
	void setBonusMaxHealth(final float bonusMaxHealth);

	/**
	 * Add an amount to the current bonus max health.
	 *
	 * @param healthToAdd The amount of health to add
	 */
	void addBonusMaxHealth(final float healthToAdd);

	/**
	 * Synchronise the entity's max health to watching clients.
	 */
	void synchronise();
}
