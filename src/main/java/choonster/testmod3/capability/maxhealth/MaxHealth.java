package choonster.testmod3.capability.maxhealth;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Objects;

/**
 * Default implementation of {@link IMaxHealth}.
 *
 * @author Choonster
 */
public class MaxHealth implements IMaxHealth {
	private static final Logger LOGGER = LogUtils.getLogger();

	/**
	 * The ID of the {@link AttributeModifier}.
	 */
	protected static final Identifier MODIFIER_ID = Identifier.fromNamespaceAndPath(TestMod3.MODID, "bonus_max_health");

	public static MaxHealth empty(@Nullable final LivingEntity entity) {
		return new MaxHealth(entity);
	}

	public static Codec<MaxHealth> codec(@Nullable final LivingEntity entity) {
		return Codec.FLOAT.xmap(
				(bonusMaxHealth) -> new MaxHealth(entity, bonusMaxHealth),
				MaxHealth::getBonusMaxHealth
		);
	}

	/**
	 * The entity this is attached to.
	 */
	@Nullable
	private final LivingEntity entity;

	/**
	 * The bonus max health.
	 */
	private float bonusMaxHealth;

	private MaxHealth(@Nullable final LivingEntity entity) {
		this(entity, 0);
	}

	private MaxHealth(@Nullable final LivingEntity entity, final float bonusMaxHealth) {
		this.entity = entity;
		this.bonusMaxHealth = bonusMaxHealth;
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
		if (entity != null && entity.level() instanceof final ServerLevel serverLevel) {
			final var entityMaxHealthAttribute = entity.getAttribute(Attributes.MAX_HEALTH);
			final var packet = new ClientboundUpdateAttributesPacket(entity.getId(), Collections.singleton(entityMaxHealthAttribute));

			serverLevel.getChunkSource().sendToTrackingPlayers(entity, packet);
		}
	}

	/**
	 * Create the {@link AttributeModifier}.
	 *
	 * @return The AttributeModifier
	 */
	protected AttributeModifier createModifier() {
		return new AttributeModifier(MODIFIER_ID, getBonusMaxHealth(), AttributeModifier.Operation.ADD_VALUE);
	}

	/**
	 * Called when the bonus max health changes to re-apply the {@link AttributeModifier}.
	 */
	protected void onBonusMaxHealthChanged() {
		if (entity == null) {
			return;
		}

		final var entityMaxHealthAttribute = Objects.requireNonNull(entity.getAttribute(Attributes.MAX_HEALTH));

		final var modifier = createModifier();

		final var newAmount = getBonusMaxHealth();
		final float oldAmount;

		final var oldModifier = entityMaxHealthAttribute.getModifier(MODIFIER_ID);
		if (oldModifier != null) {
			entityMaxHealthAttribute.removeModifier(MODIFIER_ID);

			oldAmount = (float) oldModifier.amount();

			LOGGER.debug(MaxHealthCapability.LOG_MARKER, "Max Health Changed! Entity: {} - Old: {} - New: {}", entity, MaxHealthCapability.formatMaxHealth(oldAmount), MaxHealthCapability.formatMaxHealth(newAmount));
		} else {
			oldAmount = 0.0f;

			LOGGER.debug(MaxHealthCapability.LOG_MARKER, "Max Health Added! Entity: {} - New: {}", entity, MaxHealthCapability.formatMaxHealth(newAmount));
		}

		entityMaxHealthAttribute.addTransientModifier(modifier);

		final var amountToHeal = newAmount - oldAmount;
		if (amountToHeal > 0) {
			entity.heal(amountToHeal);
		}
	}
}
