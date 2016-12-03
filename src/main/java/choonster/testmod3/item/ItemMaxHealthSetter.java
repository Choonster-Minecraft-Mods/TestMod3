package choonster.testmod3.item;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.CapabilityMaxHealth;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * An item that adds or removes max health from entities right clicked with it using the {@link IMaxHealth} capability.
 *
 * @author Choonster
 */
public class ItemMaxHealthSetter extends ItemTestMod3 {
	public ItemMaxHealthSetter() {
		super("max_health_setter_item");
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
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
