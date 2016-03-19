package com.choonster.testmod3.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
	 * The amount to multiply the use time by to determine the charge when firing arrows.
	 */
	private final int CHARGE_MULTIPLIER = 5;

	public ItemContinuousBow(String itemName) {
		super(itemName);
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
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		int charge = (getMaxItemUseDuration(stack) - timeLeft) * CHARGE_MULTIPLIER;
		fireArrow(stack, worldIn, entityLiving, charge);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		int charge = getMaxItemUseDuration(stack) * CHARGE_MULTIPLIER;
		fireArrow(stack, worldIn, entityLiving, charge);

		return stack;
	}
}
