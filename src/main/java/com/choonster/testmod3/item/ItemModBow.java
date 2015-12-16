package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.util.Constants;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

/**
 * A bow that uses custom models identical to the vanilla ones.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2576588-custom-bow-wont-load-model
 */
public class ItemModBow extends ItemBow {
	public ItemModBow() {
		setUnlocalizedName("modBow");
		setCreativeTab(TestMod3.creativeTab);
	}

	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		if (player.isUsingItem()) {
			int useTime = stack.getMaxItemUseDuration() - useRemaining;

			if (useTime >= 18) {
				return new ModelResourceLocation(Constants.RESOURCE_PREFIX + "bow_pulling_2", "inventory");
			} else if (useTime > 13) {
				return new ModelResourceLocation(Constants.RESOURCE_PREFIX + "bow_pulling_1", "inventory");
			} else if (useTime > 0) {
				return new ModelResourceLocation(Constants.RESOURCE_PREFIX + "bow_pulling_0", "inventory");
			}
		}

		return null;
	}
}
