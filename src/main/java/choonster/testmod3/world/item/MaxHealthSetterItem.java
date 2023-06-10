package choonster.testmod3.world.item;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.MaxHealthCapability;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.CapabilityNotPresentException;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * An item that adds or removes max health from entities right-clicked with it using the {@link IMaxHealth} capability.
 *
 * @author Choonster
 */
public class MaxHealthSetterItem extends Item {
	public MaxHealthSetterItem(final Item.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("resource")
	@Override
	public InteractionResult interactLivingEntity(final ItemStack stack, final Player player, final LivingEntity target, final InteractionHand hand) {
		if (!player.level().isClientSide) {
			final var maxHealth = MaxHealthCapability.getMaxHealth(target).orElseThrow(CapabilityNotPresentException::new);
			final var healthToAdd = player.isShiftKeyDown() ? -1.0f : 1.0f;

			maxHealth.addBonusMaxHealth(healthToAdd);

			player.sendSystemMessage(
					Component.translatable(
							TestMod3Lang.MESSAGE_MAX_HEALTH_ADD.getTranslationKey(),
							target.getDisplayName(),
							healthToAdd
					)
			);
		}

		return InteractionResult.SUCCESS;
	}
}
