package com.choonster.testmod3.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * An item that records how many times it's used to right click an entity.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2451199-1-8-iteminteractionforentity-with-nbt-bug
 *
 * @author Choonster
 */
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
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
		if (!playerIn.worldObj.isRemote) {
			int count = getInteractCount(stack) + 1;
			stack.getTagCompound().setInteger("Count", count);

			playerIn.addChatComponentMessage(new TextComponentTranslation("message.testmod3:entityInteractCount", count));
		}

		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (!playerIn.worldObj.isRemote) {
			int count = getInteractCount(itemStackIn);

			playerIn.addChatComponentMessage(new TextComponentTranslation("message.testmod3:entityInteractCount", count));
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}
}
