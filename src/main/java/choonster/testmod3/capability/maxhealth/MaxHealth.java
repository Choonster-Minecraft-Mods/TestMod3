package choonster.testmod3.capability.maxhealth;

import choonster.testmod3.Logger;
import choonster.testmod3.util.Constants;
import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Default implementation of {@link IMaxHealth}.
 *
 * @author Choonster
 */
public class MaxHealth implements IMaxHealth {
	/**
	 * The ID of the {@link AttributeModifier}.
	 */
	protected static final UUID MODIFIER_ID = UUID.fromString("d5d0d878-b3c2-469b-ba89-ac01c0635a9c");

	/**
	 * The name of the {@link AttributeModifier}.
	 */
	protected static final String MODIFIER_NAME = "Bonus Max Health";

	/**
	 * The minimum max health a player can have.
	 */
	protected static final float MIN_AMOUNT = 2.0f;

	/**
	 * The entity this is attached to.
	 */
	private final EntityLivingBase entity;

	/**
	 * The bonus max health.
	 */
	private float bonusMaxHealth;

	/**
	 * The dummy max health attribute. Used to avoid setting the entity's actual attribute to an invalid value.
	 */
	private final IAttributeInstance dummyMaxHealthAttribute = new AttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH);


	public MaxHealth(@Nullable EntityLivingBase entity) {
		this.entity = entity;
	}

	/**
	 * Get the bonus max health.
	 *
	 * @return The bonus max health
	 */
	@Override
	public final float getBonusMaxHealth() {
		return bonusMaxHealth;
	}

	/**
	 * Set the bonus max health.
	 *
	 * @param bonusMaxHealth The bonus max health
	 */
	@Override
	public final void setBonusMaxHealth(float bonusMaxHealth) {
		this.bonusMaxHealth = bonusMaxHealth;

		onBonusMaxHealthChanged();
	}

	/**
	 * Add an amount to the current bonus max health.
	 *
	 * @param healthToAdd The amount of health to add
	 */
	@Override
	public final void addBonusMaxHealth(float healthToAdd) {
		setBonusMaxHealth(getBonusMaxHealth() + healthToAdd);
	}

	/**
	 * Create the {@link AttributeModifier}.
	 *
	 * @return The AttributeModifier
	 */
	protected AttributeModifier createModifier() {
		return new AttributeModifier(MODIFIER_ID, MODIFIER_NAME, getBonusMaxHealth(), Constants.ATTRIBUTE_MODIFIER_OPERATION_ADD);
	}

	/**
	 * Called when the bonus max health changes to re-apply the {@link AttributeModifier}.
	 */
	protected void onBonusMaxHealthChanged() {
		if (entity == null) return;

		final IAttributeInstance entityMaxHealthAttribute;
		entityMaxHealthAttribute = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);

		// Remove all modifiers from the dummy attribute
		dummyMaxHealthAttribute.getModifiers().stream().forEach(dummyMaxHealthAttribute::removeModifier);

		// Copy the base value and modifiers except this class's from the entity's attribute to the dummy attribute
		dummyMaxHealthAttribute.setBaseValue(entityMaxHealthAttribute.getBaseValue());
		entityMaxHealthAttribute.getModifiers().stream().filter(modifier -> !modifier.getID().equals(MODIFIER_ID)).forEach(dummyMaxHealthAttribute::applyModifier);

		AttributeModifier modifier = createModifier();
		dummyMaxHealthAttribute.applyModifier(modifier);

		// Increment bonus max health by 0.5 until the max health is at least 2.0 (1 heart).
		// We do this to avoid setting the entity's max health to 0, which would kill it (and prevent it from respawning if it's a player).
		// The attribute itself will prevent its value from exceeding the maximum, so adding more than the maximum max health is harmless.
		while (dummyMaxHealthAttribute.getAttributeValue() < MIN_AMOUNT) {
			dummyMaxHealthAttribute.removeModifier(modifier);
			bonusMaxHealth += 0.5f;
			modifier = createModifier();
			dummyMaxHealthAttribute.applyModifier(modifier);
		}

		final float newAmount = getBonusMaxHealth();
		final float oldAmount;

		final AttributeModifier oldModifier = entityMaxHealthAttribute.getModifier(MODIFIER_ID);
		if (oldModifier != null) {
			entityMaxHealthAttribute.removeModifier(oldModifier);

			oldAmount = (float) oldModifier.getAmount();

			Logger.debug(CapabilityMaxHealth.LOG_MARKER, "Max Health Changed! Entity: %s - Old: %s - New: %s", entity, CapabilityMaxHealth.formatMaxHealth(oldAmount), CapabilityMaxHealth.formatMaxHealth(newAmount));
		} else {
			oldAmount = 0.0f;

			Logger.debug(CapabilityMaxHealth.LOG_MARKER, "Max Health Added! Entity: %s - New: %s", entity, CapabilityMaxHealth.formatMaxHealth(newAmount));
		}

		entityMaxHealthAttribute.applyModifier(modifier);

		final float amountToHeal = newAmount - oldAmount;
		if (amountToHeal > 0) {
			entity.heal(amountToHeal);
		}
	}
}
