package com.choonster.testmod3.item;

import com.choonster.testmod3.capability.lastusetime.CapabilityLastUseTime;
import com.choonster.testmod3.capability.lastusetime.LastUseTime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * A slingshot that fires Snowballs when used.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2483633-custom-bow-animation-and-projectiles
 *
 * @author Vastatio, Choonster
 */
public class ItemSlingshot extends ItemSnowballLauncher {
	public ItemSlingshot() {
		super("slingshot");
		CapabilityLastUseTime.TicksSinceLastUseGetter.addToItem(this);
	}

	/**
	 * Get the cooldown of the launcher (in ticks).
	 *
	 * @param launcher The launcher
	 * @return The cooldown of the launcher (in ticks), or 0 if there is none
	 */
	@Override
	protected int getCooldown(ItemStack launcher) {
		return 0;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new CapabilityLastUseTime.Provider(new LastUseTime(false));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		final ActionResult<ItemStack> result = super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);

		if (result.getType() == EnumActionResult.SUCCESS) {
			CapabilityLastUseTime.updateLastUseTime(playerIn, itemStackIn, hand);
		}

		return result;
	}
}
