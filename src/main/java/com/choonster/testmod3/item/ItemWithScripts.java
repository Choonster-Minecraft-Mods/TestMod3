package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

import java.util.function.Function;

public abstract class ItemWithScripts extends ItemTestMod3 {
	private final Function<Integer, String> scriptFunction;

	public ItemWithScripts(Function<Integer, String> scriptFunction, String itemName) {
		super(itemName);
		this.scriptFunction = scriptFunction;
		setHasSubtypes(true);
	}

	private int getNumber(ItemStack stack) {
		if (stack.hasTagCompound()) {
			return stack.getTagCompound().getInteger("Number");
		} else {
			return -1337;
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return super.getItemStackDisplayName(stack) + scriptFunction.apply(stack.getItemDamage());
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (!worldIn.isRemote) {
			playerIn.addChatComponentMessage(new ChatComponentTranslation("message." + getRegistryName() + ".rightClick", scriptFunction.apply(getNumber(itemStackIn))));
		}

		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}
}
