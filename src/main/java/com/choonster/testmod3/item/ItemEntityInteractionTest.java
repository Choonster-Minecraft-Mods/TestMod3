package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2451199-1-8-iteminteractionforentity-with-nbt-bug
public class ItemEntityInteractionTest extends Item {
	public ItemEntityInteractionTest() {
		setCreativeTab(TestMod3.creativeTab);
		setUnlocalizedName("entityInteractionTest");
	}

	private int getInteractCount(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}

		NBTTagCompound tagCompound = stack.getTagCompound();

		if (!tagCompound.hasKey("Count", Constants.NBT.TAG_INT)) {
			tagCompound.setInteger("Count", 0);
		}

		return tagCompound.getInteger("Count");
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
		int count = getInteractCount(stack) + 1;
		stack.getTagCompound().setInteger("Count", count);

		if (!playerIn.worldObj.isRemote) {
			playerIn.addChatComponentMessage(new ChatComponentTranslation("message.entityInteractCount", count));
		}

		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (!playerIn.worldObj.isRemote) {
			int count = getInteractCount(itemStackIn);
			playerIn.addChatComponentMessage(new ChatComponentTranslation("message.entityInteractCount", count));
		}

		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}
}
