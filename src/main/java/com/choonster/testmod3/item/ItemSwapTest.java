package com.choonster.testmod3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.List;

/**
 * An item that's converted to another when shift-right clicked.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34244.0.html
 *
 * @author Choonster
 */
public class ItemSwapTest extends ItemTestMod3 {
	private ItemStack otherItem;

	public ItemSwapTest(String name) {
		super("swapTest" + name);
	}

	public boolean hasOtherItem() {
		return otherItem != null;
	}

	public void setOtherItem(ItemStack otherItem) {
		this.otherItem = otherItem;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);

		if (hasOtherItem()) {
			tooltip.add(I18n.translateToLocalFormatted("item.testmod3:swapTest.withItem.desc", otherItem.getDisplayName()));
		} else {
			tooltip.add(I18n.translateToLocal("item.testmod3:swapTest.withoutItem.desc"));
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (hasOtherItem() && playerIn.isSneaking()) {
			return new ActionResult<>(EnumActionResult.SUCCESS, otherItem.copy());
		}

		return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
	}
}
