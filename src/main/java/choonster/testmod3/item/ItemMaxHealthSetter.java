package choonster.testmod3.item;

import choonster.testmod3.capability.maxhealth.CapabilityMaxHealth;
import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
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
		super("maxHealthSetterItem");
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
		if (!playerIn.worldObj.isRemote) {
			final IMaxHealth maxHealth = CapabilityMaxHealth.getMaxHealth(target);

			final float healthToAdd = playerIn.isSneaking() ? -1.0f : 1.0f;
			maxHealth.addBonusMaxHealth(healthToAdd);

			playerIn.addChatMessage(new TextComponentTranslation("message.testmod3:maxHealth.add", target.getDisplayName(), healthToAdd));
		}

		return true;
	}
}
