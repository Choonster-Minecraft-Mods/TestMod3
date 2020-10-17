package choonster.testmod3.item;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.MaxHealthCapability;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * An item that adds or removes max health from entities right clicked with it using the {@link IMaxHealth} capability.
 *
 * @author Choonster
 */
public class MaxHealthSetterItem extends Item {
	public MaxHealthSetterItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType itemInteractionForEntity(final ItemStack stack, final PlayerEntity player, final LivingEntity target, final Hand hand) {
		if (!player.world.isRemote) {
			MaxHealthCapability.getMaxHealth(target).ifPresent(maxHealth -> {
				final float healthToAdd = player.isSneaking() ? -1.0f : 1.0f;

				maxHealth.addBonusMaxHealth(healthToAdd);

				player.sendMessage(new TranslationTextComponent("message.testmod3.max_health.add", target.getDisplayName(), healthToAdd), Util.DUMMY_UUID);
			});
		}

		return ActionResultType.SUCCESS;
	}
}
