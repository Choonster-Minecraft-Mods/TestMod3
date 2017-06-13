package choonster.testmod3.item;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.CapabilityMaxHealth;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * An item that tells the player the current max health and the bonus max health provided by the entity's {@link IMaxHealth} when right clicked on an entity.
 *
 * @author Choonster
 */
public class ItemMaxHealthGetter extends ItemTestMod3 {
	public ItemMaxHealthGetter() {
		super("max_health_getter_item");
	}

	@Override
	public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer playerIn, final EntityLivingBase target, final EnumHand hand) {
		if (!playerIn.world.isRemote) {
			final IMaxHealth maxHealth = CapabilityMaxHealth.getMaxHealth(target);

			if (maxHealth != null) {
				playerIn.sendMessage(new TextComponentTranslation("message.testmod3:max_health.get", target.getDisplayName(), target.getMaxHealth(), maxHealth.getBonusMaxHealth()));
			}
		}

		return true;
	}
}
