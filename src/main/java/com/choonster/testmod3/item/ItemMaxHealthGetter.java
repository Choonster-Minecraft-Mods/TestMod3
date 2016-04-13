package com.choonster.testmod3.item;

import com.choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import com.choonster.testmod3.capability.maxhealth.CapabilityMaxHealth;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * An item that tells the player the current max health and the bonus max health provided by the entity's {@link IMaxHealth} when right on an entity.
 *
 * @author Choonster
 */
public class ItemMaxHealthGetter extends ItemTestMod3 {
	public ItemMaxHealthGetter() {
		super("maxHealthGetterItem");
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
		if (!playerIn.worldObj.isRemote) {
			final IMaxHealth maxHealth = CapabilityMaxHealth.getMaxHealth(target);

			playerIn.addChatMessage(new TextComponentTranslation("message.testmod3:maxHealth.get", target.getDisplayName(), target.getMaxHealth(), maxHealth.getBonusMaxHealth()));
		}

		return true;
	}
}
