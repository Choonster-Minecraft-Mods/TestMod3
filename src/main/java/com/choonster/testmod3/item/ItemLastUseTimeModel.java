package com.choonster.testmod3.item;

import com.choonster.testmod3.capability.lastusetime.CapabilityLastUseTime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * An Item with a different model depending on how long ago it was last used.
 * <p>
 * Doesn't use {@link CooldownTracker} because that would prevent using the item again before the cooldown has expired.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2470478-how-to-do-a-custom-bow-animation
 *
 * @author Choonster
 */
public class ItemLastUseTimeModel extends ItemTestMod3 {

	public ItemLastUseTimeModel(String itemName) {
		super(itemName);
		CapabilityLastUseTime.TicksSinceLastUseGetter.addToItem(this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new CapabilityLastUseTime.Provider();
	}
}
