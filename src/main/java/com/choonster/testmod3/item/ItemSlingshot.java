package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * A slingshot that fires Snowballs when used.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2483633-custom-bow-animation-and-projectiles
 *
 * @author Vastatio, Choonster
 */
public class ItemSlingshot extends ItemTestMod3 {
	public ItemSlingshot() {
		super("slingshot");
	}

	private ItemStack getAmmoItemStack(EntityPlayer player)
	{
		if (this.isAmmoItem(player.getHeldItem(EnumHand.OFF_HAND)))
		{
			return player.getHeldItem(EnumHand.OFF_HAND);
		}
		else if (this.isAmmoItem(player.getHeldItem(EnumHand.MAIN_HAND)))
		{
			return player.getHeldItem(EnumHand.MAIN_HAND);
		}
		else
		{
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
			{
				ItemStack itemstack = player.inventory.getStackInSlot(i);

				if (this.isAmmoItem(itemstack))
				{
					return itemstack;
				}
			}

			return null;
		}
	}

	protected boolean isAmmoItem(ItemStack stack)
	{
		return stack != null && stack.getItem() instanceof ItemArrow;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (player.capabilities.isCreativeMode || player.inventory.consumeInventoryItem(Items.snowball)) {
			setLastUseTime(stack, world.getTotalWorldTime());
			world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			if (!world.isRemote) {
				world.spawnEntityInWorld(new EntitySnowball(world, player));
			}

			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}

		return new ActionResult<>(EnumActionResult.FAIL, stack);
	}

	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - getLastUseTime(stack);

		if (ticksSinceLastUse < 20) {
			return new ModelResourceLocation(TestMod3.MODID + ":slingshot_pulled", "inventory");
		}

		return null;
	}

	private void setLastUseTime(ItemStack stack, long time) {
		stack.setTagInfo("LastUse", new NBTTagLong(time));
	}

	private long getLastUseTime(ItemStack stack) {
		return stack.hasTagCompound() ? stack.getTagCompound().getLong("LastUse") : 0;
	}
}
