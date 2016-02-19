package com.choonster.testmod3.item;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * A bow that fires continuously while right click is held.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,36707.0.html
 *
 * @author Choonster
 */
public class ItemContinuousBow extends ItemModBow {

	/**
	 * How often the launcher fires (in ticks)
	 */
	private final long FIRE_RATE = 10;

	/**
	 * The charge of the arrows fired by this bow.
	 */
	private final int CHARGE = 72000;

	public ItemContinuousBow(String itemName) {
		super(itemName);
	}

	/**
	 * Has it been at least FIRE_RATE ticks since the bow was last used?
	 *
	 * @param stack The now ItemStack
	 * @param world The World to check the time against
	 * @return True if the ItemStack was last used at least FIRE_RATE ticks ago or if it has never been used
	 */
	protected boolean isOffCooldown(ItemStack stack, World world) {
		return !stack.hasTagCompound() || (world.getTotalWorldTime() - stack.getTagCompound().getLong("LastUseTime")) >= FIRE_RATE;
	}

	/**
	 * Set the bow's last use time to the specified time.
	 *
	 * @param stack The bow ItemStack
	 * @param time  The time
	 */
	protected void setLastUseTime(ItemStack stack, long time) {
		stack.setTagInfo("LastUseTime", new NBTTagLong(time));
	}

	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		if (player.isUsingItem()) {
			int useTime = stack.getMaxItemUseDuration() - useRemaining;

			if (useTime >= 9) {
				return new ModelResourceLocation(getModelLocation(), "pulling_2");
			} else if (useTime > 6) {
				return new ModelResourceLocation(getModelLocation(), "pulling_1");
			} else if (useTime > 0) {
				return new ModelResourceLocation(getModelLocation(), "pulling_0");
			}
		}

		return null;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 10;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
		// No-op
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		Optional<ItemStack> eventResult = nockArrow(itemStackIn, playerIn);
		if (eventResult.isPresent()) return eventResult.get();

		if (isOffCooldown(itemStackIn, worldIn) && (!playerNeedsAmmo(itemStackIn, playerIn) || playerHasAmmo(itemStackIn, playerIn))) {
			fireArrow(itemStackIn, worldIn, playerIn, CHARGE);
		}

		return itemStackIn;
	}
}
