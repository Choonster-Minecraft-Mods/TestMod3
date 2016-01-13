package com.choonster.testmod3.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.world.World;

/**
 * An Item that fires Snowballs at a fixed rate while right click is held
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,32389.0.html
 *
 * @author Choonster
 */
public class ItemSnowballLauncher extends ItemTestMod3 {

	/**
	 * How often the launcher fires (in ticks)
	 */
	public static final int FIRE_RATE = 20;

	public ItemSnowballLauncher() {
		super("snowballLauncher");
	}

	/**
	 * Has it been at least FIRE_RATE ticks since the launcher was last used?
	 *
	 * @param stack The launcher ItemStack
	 * @param world The World to check the time against
	 * @return True if the ItemStack was last used at least FIRE_RATE ticks ago or if it has never been used
	 */
	private boolean isOffCooldown(ItemStack stack, World world) {
		return !stack.hasTagCompound() || (world.getTotalWorldTime() - stack.getTagCompound().getLong("LastUseTime")) >= FIRE_RATE;
	}

	/**
	 * Does the player need ammunition to fire the launcher?
	 *
	 * @param stack  The launcher ItemStack
	 * @param player The player to check
	 * @return True if the player is not in creative mode and the launcher doesn't have the Infinity enchantment
	 */
	private boolean playerNeedsAmmo(ItemStack stack, EntityPlayer player) {
		return !player.capabilities.isCreativeMode && EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) == 0;
	}

	/**
	 * Set the launcher's last use time to the specified time.
	 *
	 * @param stack The launcher ItemStack
	 * @param time  The time
	 */
	private void setLastUseTime(ItemStack stack, long time) {
		stack.setTagInfo("LastUseTime", new NBTTagLong(time));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		boolean offCooldown = isOffCooldown(itemStackIn, worldIn);
		boolean needsAmmo = playerNeedsAmmo(itemStackIn, playerIn);

		if (offCooldown && (!needsAmmo || playerIn.inventory.consumeInventoryItem(Items.snowball))) {
			setLastUseTime(itemStackIn, worldIn.getTotalWorldTime());

			worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			if (!worldIn.isRemote) {
				worldIn.spawnEntityInWorld(new EntitySnowball(worldIn, playerIn));
			}

			if (needsAmmo) {
				playerIn.inventoryContainer.detectAndSendChanges();
			}
		}

		return itemStackIn;
	}
}
