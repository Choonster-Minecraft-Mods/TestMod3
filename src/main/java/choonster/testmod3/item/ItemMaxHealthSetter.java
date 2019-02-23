package choonster.testmod3.item;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.CapabilityMaxHealth;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * An item that adds or removes max health from entities right clicked with it using the {@link IMaxHealth} capability.
 *
 * @author Choonster
 */
public class ItemMaxHealthSetter extends Item {
	@Override
	public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer playerIn, final EntityLivingBase target, final EnumHand hand) {
		if (!playerIn.world.isRemote) {
			final IMaxHealth maxHealth = CapabilityMaxHealth.getMaxHealth(target);

			if (maxHealth != null) {
				final float healthToAdd = playerIn.isSneaking() ? -1.0f : 1.0f;

				maxHealth.addBonusMaxHealth(healthToAdd);

				playerIn.sendMessage(new TextComponentTranslation("message.testmod3:max_health.add", target.getDisplayName(), healthToAdd));
			}
		}

		return true;
	}
}
