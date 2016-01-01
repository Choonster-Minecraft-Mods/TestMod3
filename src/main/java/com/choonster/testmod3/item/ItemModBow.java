package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.entity.EntityModArrow;
import com.choonster.testmod3.init.ModItems;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A bow that uses custom models identical to the vanilla ones.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2576588-custom-bow-wont-load-model
 */
public class ItemModBow extends ItemBow {
	public ItemModBow() {
		setUnlocalizedName("modBow");
		setCreativeTab(TestMod3.creativeTab);
	}

	/**
	 * Get the location of the blockstates file used for this item's models
	 *
	 * @return The location
	 */
	public ResourceLocation getModelLocation() {
		return Item.itemRegistry.getNameForObject(this);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		if (player.isUsingItem()) {
			int useTime = stack.getMaxItemUseDuration() - useRemaining;

			if (useTime >= 18) {
				return new ModelResourceLocation(getModelLocation(), "pulling_2");
			} else if (useTime > 13) {
				return new ModelResourceLocation(getModelLocation(), "pulling_1");
			} else if (useTime > 0) {
				return new ModelResourceLocation(getModelLocation(), "pulling_0");
			}
		}

		return null;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
		int charge = this.getMaxItemUseDuration(stack) - timeLeft;
		ArrowLooseEvent event = new ArrowLooseEvent(playerIn, stack, charge);
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return;
		charge = event.charge;

		boolean noAmmoRequired = playerIn.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;

		if (noAmmoRequired || playerIn.inventory.hasItem(ModItems.modArrow)) {
			float velocity = (float) charge / 20.0f;
			velocity = (velocity * velocity + velocity * 2.0f) / 3.0f;

			if (velocity < 0.1f) {
				return;
			}

			if (velocity > 1.0f) {
				velocity = 1.0f;
			}

			EntityModArrow entityArrow = new EntityModArrow(worldIn, playerIn, velocity * 2.0f);

			if (velocity == 1.0f) {
				entityArrow.setIsCritical(true);
			}

			int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
			if (powerLevel > 0) {
				entityArrow.setDamage(entityArrow.getDamage() + powerLevel * 0.5D + 0.5D);
			}

			int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
			if (punchLevel > 0) {
				entityArrow.setKnockbackStrength(punchLevel);
			}

			if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0) {
				entityArrow.setFire(100);
			}

			stack.damageItem(1, playerIn);
			worldIn.playSoundAtEntity(playerIn, "random.bow", 1.0f, 1.0f / (itemRand.nextFloat() * 0.4f + 1.2f) + velocity * 0.5f);

			if (noAmmoRequired) {
				entityArrow.canBePickedUp = 2;
			} else {
				playerIn.inventory.consumeInventoryItem(ModItems.modArrow);
			}

			playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);

			if (!worldIn.isRemote) {
				worldIn.spawnEntityInWorld(entityArrow);
			}
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		ArrowNockEvent event = new ArrowNockEvent(playerIn, itemStackIn);
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return event.result;

		if (playerIn.capabilities.isCreativeMode || playerIn.inventory.hasItem(ModItems.modArrow)) {
			playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		}

		return itemStackIn;
	}
}
