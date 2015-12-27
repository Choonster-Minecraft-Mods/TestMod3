package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	/**
	 * Get the location of the blockstates file used for this item's models
	 *
	 * @return The location
	 */
	public ResourceLocation getModelLocation() {
		return Item.itemRegistry.getNameForObject(this);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		if (player.isUsingItem()) {
			int useTime = stack.getMaxItemUseDuration() - useRemaining;

			if (useTime >= 18) {
				return new ModelResourceLocation(getModelLocation(), "pulling_2");
			} else if (useTime > 13) {
				return new ModelResourceLocation(getModelLocation(), "pulling_1");
			} else if (useTime > 0) {
				return new ModelResourceLocation(getModelLocation(), "pulling_0");
			}
		}

		return null;
	}
}
