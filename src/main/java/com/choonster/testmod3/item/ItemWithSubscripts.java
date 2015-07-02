package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.util.StringUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class ItemWithSubscripts extends Item {
	public ItemWithSubscripts() {
		setCreativeTab(TestMod3.creativeTab);
		setUnlocalizedName("subscripts");
		setHasSubtypes(true);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return super.getItemStackDisplayName(stack) + StringUtils.subscript(stack.getItemDamage());
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (!worldIn.isRemote) {
			playerIn.addChatComponentMessage(new ChatComponentTranslation("message.subscripts.rightClick", StringUtils.subscript(itemStackIn.getItemDamage())));
		}

		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}
}
