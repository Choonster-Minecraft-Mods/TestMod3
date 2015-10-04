package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * An item that's converted to another when shift-right clicked.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34244.0.html
 */
public class ItemSwapTest extends Item {
	private ItemStack otherItem;

	public ItemSwapTest(String name) {
		setCreativeTab(TestMod3.creativeTab);
		setUnlocalizedName("swapTest" + name);
	}

	public boolean hasOtherItem() {
		return otherItem != null;
	}

	public void setOtherItem(ItemStack otherItem) {
		this.otherItem = otherItem;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);

		if (hasOtherItem()) {
			tooltip.add(StatCollector.translateToLocalFormatted("item.swapTestWithItem.desc", otherItem.getDisplayName()));
		} else {
			tooltip.add(StatCollector.translateToLocal("item.swapTestWithoutItem.desc"));
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (hasOtherItem() && playerIn.isSneaking()) {
			return otherItem.copy();
		}

		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}
}
