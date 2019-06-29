package choonster.testmod3.item;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.MaxHealthCapability;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;

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
	public boolean itemInteractionForEntity(final ItemStack stack, final PlayerEntity player, final LivingEntity target, final Hand hand) {
		if (!player.world.isRemote) {
			MaxHealthCapability.getMaxHealth(target).ifPresent(maxHealth -> {
				player.sendMessage(new TranslationTextComponent("message.testmod3.max_health.get", target.getDisplayName(), target.getMaxHealth(), maxHealth.getBonusMaxHealth()));
			});
		}

		return true;
	}
}
