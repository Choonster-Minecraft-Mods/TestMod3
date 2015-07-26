package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.world.World;

/**
 * Based on Vastatio's ARKSlingshot code.
 *
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2483633-custom-bow-animation-and-projectiles
 */
public class ItemSlingshot extends Item {
	public ItemSlingshot() {
		setCreativeTab(TestMod3.creativeTab);
		setUnlocalizedName("slingshot");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer p) {
		if (p.capabilities.isCreativeMode
				|| p.inventory
				.consumeInventoryItem(Items.snowball)) {
			setLastUseTime(stack, w.getTotalWorldTime());
			w.playSoundAtEntity(p, "random.bow", 0.5F,
					0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if (!w.isRemote)
				w.spawnEntityInWorld(new EntitySnowball(w, p));
		}
		return super.onItemRightClick(stack, w, p);
	}

	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player,
										  int useRemaining) {
		long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - getLastUseTime(stack);
		if (ticksSinceLastUse < 20)
			return new ModelResourceLocation(TestMod3.MODID + ":slingshot_pulled",
					"inventory");
		else return null;
	}

	private void setLastUseTime(ItemStack stack, long time) {
		stack.setTagInfo("LastUse", new NBTTagLong(time));
	}

	private long getLastUseTime(ItemStack stack) {
		return stack.hasTagCompound() ? stack.getTagCompound().getLong(
				"LastUse") : 0;
	}
}
