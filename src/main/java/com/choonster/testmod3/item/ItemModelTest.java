package com.choonster.testmod3.item;

import com.choonster.testmod3.util.Constants;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.world.World;

/**
 * An Item with a different model depending on how long ago it was last used.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2470478-how-to-do-a-custom-bow-animation
 *
 * @author Choonster
 */
public class ItemModelTest extends ItemTestMod3 {
	public ItemModelTest() {
		super("modeltest");
	}

	private long getLastUseTime(ItemStack stack) {
		return stack.hasTagCompound() ? stack.getTagCompound().getLong("LastUse") : 0;
	}

	private void setLastUseTime(ItemStack stack, long time) {
		stack.setTagInfo("LastUse", new NBTTagLong(time));
	}

	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - getLastUseTime(stack);

		if (ticksSinceLastUse < 20) {
			return new ModelResourceLocation(Constants.RESOURCE_PREFIX + "modeltest_0", "inventory");
		} else if (ticksSinceLastUse < 40) {
			return new ModelResourceLocation(Constants.RESOURCE_PREFIX + "modeltest_1", "inventory");
		} else if (ticksSinceLastUse < 60) {
			return new ModelResourceLocation(Constants.RESOURCE_PREFIX + "modeltest_2", "inventory");
		} else {
			return null;
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		setLastUseTime(itemStackIn, worldIn.getTotalWorldTime());

		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}
}
