package com.choonster.testmod3.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2451199-1-8-iteminteractionforentity-with-nbt-bug
public class ItemEntityInteractionTest extends ItemTestMod3 {
	public ItemEntityInteractionTest() {
		super("entityInteractionTest");
	}

	private int getInteractCount(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}

		return stack.getTagCompound().getInteger("Count");
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
		if (!playerIn.worldObj.isRemote) {
			int count = getInteractCount(stack) + 1;
			stack.getTagCompound().setInteger("Count", count);

			playerIn.addChatComponentMessage(new ChatComponentTranslation("message.testmod3:entityInteractCount", count));
		}

		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (!playerIn.worldObj.isRemote) {
			int count = getInteractCount(itemStackIn);

			playerIn.addChatComponentMessage(new ChatComponentTranslation("message.testmod3:entityInteractCount", count));
		}

		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}
}
