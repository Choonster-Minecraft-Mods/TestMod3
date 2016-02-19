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
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Optional;

/**
 * A bow that uses custom models identical to the vanilla ones and shoots custom arrows.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2576588-custom-bow-wont-load-model
 *
 * @author Choonster
 */
public class ItemModBow extends ItemBow {
	public ItemModBow(String itemName) {
		ItemTestMod3.setItemName(this, itemName);
		setCreativeTab(TestMod3.creativeTab);
	}

	/**
	 * Get the location of the blockstates file used for this item's models
	 *
	 * @return The location
	 */
	public String getModelLocation() {
		return getRegistryName();
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

	/**
	 * Get the ammunition fired by this bow
	 *
	 * @param stack The bow ItemStack
	 * @return The ammunition Item
	 */
	protected Item getAmmoItem(ItemStack stack) {
		return ModItems.modArrow;
	}

	/**
	 * Does the player need ammunition to fire the bow?
	 *
	 * @param stack  The bow ItemStack
	 * @param player The player to check
	 * @return True if the player is not in creative mode and the bow doesn't have the Infinity enchantment
	 */
	protected boolean playerNeedsAmmo(ItemStack stack, EntityPlayer player) {
		return !player.capabilities.isCreativeMode && EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) == 0;
	}

	/**
	 * Does the player have the ammunition to fire the bow?
	 *
	 * @param stack  The bow ItemStack
	 * @param player The player to check
	 * @return True if the player has the ammunition Item
	 */
	protected boolean playerHasAmmo(ItemStack stack, EntityPlayer player) {
		return player.inventory.hasItem(getAmmoItem(stack));
	}

	/**
	 * Nock an arrow.
	 *
	 * @param itemStackIn The bow ItemStack
	 * @param playerIn    The player
	 * @return The result of ArrowNockEvent if it was canceled.
	 */
	protected Optional<ItemStack> nockArrow(ItemStack itemStackIn, EntityPlayer playerIn) {
		ArrowNockEvent event = new ArrowNockEvent(playerIn, itemStackIn);
		if (MinecraftForge.EVENT_BUS.post(event)) return Optional.of(event.result);

		if (playerIn.capabilities.isCreativeMode || playerIn.inventory.hasItem(ModItems.modArrow)) {
			playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		}

		return Optional.empty();
	}

	/**
	 * Fire an arrow with the specified charge.
	 *
	 * @param stack  The bow ItemStack
	 * @param world  The firing player's World
	 * @param player The player firing the bow
	 * @param charge The charge of the arrow
	 */
	protected void fireArrow(ItemStack stack, World world, EntityPlayer player, int charge) {
		ArrowLooseEvent event = new ArrowLooseEvent(player, stack, charge);
		if (MinecraftForge.EVENT_BUS.post(event)) return;
		charge = event.charge;

		boolean playerNeedsAmmo = playerNeedsAmmo(stack, player);

		if (!playerNeedsAmmo || playerHasAmmo(stack, player)) {
			float velocity = (float) charge / 20.0f;
			velocity = (velocity * velocity + velocity * 2.0f) / 3.0f;

			if (velocity < 0.1f) {
				return;
			}

			if (velocity > 1.0f) {
				velocity = 1.0f;
			}

			EntityModArrow entityArrow = new EntityModArrow(world, player, velocity * 2.0f);

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

			stack.damageItem(1, player);
			world.playSoundAtEntity(player, "random.bow", 1.0f, 1.0f / (itemRand.nextFloat() * 0.4f + 1.2f) + velocity * 0.5f);

			if (playerNeedsAmmo) {
				player.inventory.consumeInventoryItem(getAmmoItem(stack));
			} else {
				entityArrow.canBePickedUp = 2;
			}

			player.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);

			if (!world.isRemote) {
				world.spawnEntityInWorld(entityArrow);
			}
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
		int charge = this.getMaxItemUseDuration(stack) - timeLeft;
		fireArrow(stack, worldIn, playerIn, charge);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		Optional<ItemStack> eventResult = nockArrow(itemStackIn, playerIn);
		if (eventResult.isPresent()) return eventResult.get();

		return itemStackIn;
	}
}
