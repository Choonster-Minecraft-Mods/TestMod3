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
	public ItemMaxHealthSetter(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer player, final EntityLivingBase target, final EnumHand hand) {
		if (!player.world.isRemote) {
			CapabilityMaxHealth.getMaxHealth(target).ifPresent(maxHealth -> {
				final float healthToAdd = player.isSneaking() ? -1.0f : 1.0f;

				maxHealth.addBonusMaxHealth(healthToAdd);

				player.sendMessage(new TextComponentTranslation("message.testmod3:max_health.add", target.getDisplayName(), healthToAdd));
			});
		}

		return true;
	}
}
