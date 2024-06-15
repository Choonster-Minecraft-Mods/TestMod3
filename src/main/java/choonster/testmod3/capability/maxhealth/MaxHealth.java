package choonster.testmod3.capability.maxhealth;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import com.mojang.logging.LogUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.FloatTag;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Objects;

/**
 * Default implementation of {@link IMaxHealth}.
 *
 * @author Choonster
 */
public class MaxHealth implements IMaxHealth, INBTSerializable<FloatTag> {
	private static final Logger LOGGER = LogUtils.getLogger();

	/**
	 * The ID of the {@link AttributeModifier}.
	 */
	protected static final ResourceLocation MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(TestMod3.MODID, "bonus_max_health");

	/**
	 * The entity this is attached to.
	 */
	@Nullable
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
		if (entity != null && !entity.getCommandSenderWorld().isClientSide) {
			final var entityMaxHealthAttribute = entity.getAttribute(Attributes.MAX_HEALTH);
			final var packet = new ClientboundUpdateAttributesPacket(entity.getId(), Collections.singleton(entityMaxHealthAttribute));

			((ServerLevel) entity.getCommandSenderWorld()).getChunkSource().broadcastAndSend(entity, packet);
		}
	}

	@Override
	public FloatTag serializeNBT(final HolderLookup.Provider registries) {
		return FloatTag.valueOf(bonusMaxHealth);
	}

	@Override
	public void deserializeNBT(final HolderLookup.Provider registries, final FloatTag tag) {
		bonusMaxHealth = tag.getAsFloat();
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
