package choonster.testmod3.capability.maxhealth;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.play.server.SEntityPropertiesPacket;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.UUID;

/**
 * Default implementation of {@link IMaxHealth}.
 *
 * @author Choonster
 */
public class MaxHealth implements IMaxHealth {
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * The ID of the {@link AttributeModifier}.
	 */
	protected static final UUID MODIFIER_ID = UUID.fromString("d5d0d878-b3c2-469b-ba89-ac01c0635a9c");

	/**
	 * The name of the {@link AttributeModifier}.
	 */
	protected static final String MODIFIER_NAME = "Bonus Max Health";

	/**
	 * The entity this is attached to.
	 */
	private final LivingEntity entity;

	/**
	 * The bonus max health.
	 */
	private float bonusMaxHealth;

	public MaxHealth(@Nullable final LivingEntity entity) {
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
	public final void setBonusMaxHealth(final float bonusMaxHealth) {
		this.bonusMaxHealth = bonusMaxHealth;

		onBonusMaxHealthChanged();
	}

	/**
	 * Add an amount to the current bonus max health.
	 *
	 * @param healthToAdd The amount of health to add
	 */
	@Override
	public final void addBonusMaxHealth(final float healthToAdd) {
		setBonusMaxHealth(getBonusMaxHealth() + healthToAdd);
	}

	/**
	 * Synchronise the entity's max health to watching clients.
	 */
	@Override
	public void synchronise() {
		if (entity != null && !entity.getEntityWorld().isRemote) {
			final IAttributeInstance entityMaxHealthAttribute = entity.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
			final SEntityPropertiesPacket packet = new SEntityPropertiesPacket(entity.getEntityId(), Collections.singleton(entityMaxHealthAttribute));

			((ServerWorld) entity.getEntityWorld()).getChunkProvider().sendToTrackingAndSelf(entity, packet);
		}
	}

	/**
	 * Create the {@link AttributeModifier}.
	 *
	 * @return The AttributeModifier
	 */
	protected AttributeModifier createModifier() {
		return new AttributeModifier(MODIFIER_ID, MODIFIER_NAME, getBonusMaxHealth(), AttributeModifier.Operation.ADDITION);
	}

	/**
	 * Called when the bonus max health changes to re-apply the {@link AttributeModifier}.
	 */
	protected void onBonusMaxHealthChanged() {
		if (entity == null) return;

		final IAttributeInstance entityMaxHealthAttribute = entity.getAttribute(SharedMonsterAttributes.MAX_HEALTH);

		final AttributeModifier modifier = createModifier();

		final float newAmount = getBonusMaxHealth();
		final float oldAmount;

		final AttributeModifier oldModifier = entityMaxHealthAttribute.getModifier(MODIFIER_ID);
		if (oldModifier != null) {
			entityMaxHealthAttribute.removeModifier(oldModifier);

			oldAmount = (float) oldModifier.getAmount();

			LOGGER.debug(MaxHealthCapability.LOG_MARKER, "Max Health Changed! Entity: {} - Old: {} - New: {}", entity, MaxHealthCapability.formatMaxHealth(oldAmount), MaxHealthCapability.formatMaxHealth(newAmount));
		} else {
			oldAmount = 0.0f;

			LOGGER.debug(MaxHealthCapability.LOG_MARKER, "Max Health Added! Entity: {} - New: {}", entity, MaxHealthCapability.formatMaxHealth(newAmount));
		}

		entityMaxHealthAttribute.applyModifier(modifier);

		final float amountToHeal = newAmount - oldAmount;
		if (amountToHeal > 0) {
			entity.heal(amountToHeal);
		}
	}
}
