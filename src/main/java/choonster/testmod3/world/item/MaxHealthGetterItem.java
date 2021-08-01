package choonster.testmod3.world.item;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.MaxHealthCapability;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * An item that tells the player the current max health and the bonus max health provided by the entity's {@link IMaxHealth} when right clicked on an entity.
 *
 * @author Choonster
 */
public class MaxHealthGetterItem extends Item {
	public MaxHealthGetterItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult interactLivingEntity(final ItemStack stack, final Player player, final LivingEntity target, final InteractionHand hand) {
		if (!player.level.isClientSide) {
			MaxHealthCapability.getMaxHealth(target).ifPresent(maxHealth ->
					player.sendMessage(new TranslatableComponent(TestMod3Lang.MESSAGE_MAX_HEALTH_GET.getTranslationKey(), target.getDisplayName(), target.getMaxHealth(), maxHealth.getBonusMaxHealth()), Util.NIL_UUID)
			);
		}

		return InteractionResult.SUCCESS;
	}
}
