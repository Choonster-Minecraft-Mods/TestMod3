package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	 * Get the first {@link ItemStack} of arrows in the player's inventory.
	 * <p>
	 * Copied from {@link ItemBow#func_185060_a(EntityPlayer)} because it's private.
	 *
	 * @param player The player
	 * @return The arrow ItemStack, or null if there aren't any
	 */
	protected ItemStack getArrowItemStack(EntityPlayer player) {
		// TODO: Update MCP mappings. func_185058_h_ = isArrow
		if (this.func_185058_h_(player.getHeldItem(EnumHand.OFF_HAND))) {
			return player.getHeldItem(EnumHand.OFF_HAND);
		} else if (this.func_185058_h_(player.getHeldItem(EnumHand.MAIN_HAND))) {
			return player.getHeldItem(EnumHand.MAIN_HAND);
		} else {
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
				ItemStack itemStack = player.inventory.getStackInSlot(i);

				if (this.func_185058_h_(itemStack)) {
					return itemStack;
				}
			}

			return null;
		}
	}

	/**
	 * Nock an arrow.
	 *
	 * @param bow     The bow ItemStack
	 * @param shooter The player shooting the bow
	 * @return The result of ArrowNockEvent if it was canceled.
	 */
	protected ActionResult<ItemStack> nockArrow(ItemStack bow, World world, EntityPlayer shooter, EnumHand hand) {
		boolean flag = this.getArrowItemStack(shooter) != null;

		ActionResult<ItemStack> ret = ForgeEventFactory.onArrowNock(bow, world, shooter, hand, flag);
		if (ret != null) return ret;

		if (!shooter.capabilities.isCreativeMode && !flag) {
			return new ActionResult<>(EnumActionResult.FAIL, bow);
		} else {
			shooter.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, bow);
		}
	}

	/**
	 * Fire an arrow with the specified charge.
	 *
	 * @param bow     The bow ItemStack
	 * @param world   The firing player's World
	 * @param shooter The player firing the bow
	 * @param charge  The charge of the arrow
	 */
	protected void fireArrow(ItemStack bow, World world, EntityLivingBase shooter, int charge) {
		if (!(shooter instanceof EntityPlayer)) return;

		final EntityPlayer player = (EntityPlayer) shooter;
		final boolean noArrowsRequired = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.infinity, bow) > 0;
		ItemStack arrows = this.getArrowItemStack(player);

		charge = ForgeEventFactory.onArrowLoose(bow, world, player, charge, arrows != null || noArrowsRequired);
		if (charge < 0) return;

		if (arrows != null || noArrowsRequired) {
			if (arrows == null) {
				arrows = new ItemStack(Items.arrow);
			}

			// TODO: Update MCP mappings. func_185059_b = getArrowVelocity
			final float arrowVelocity = func_185059_b(charge);

			if (arrowVelocity >= 0.1) {
				final boolean noAmmoConsumed = noArrowsRequired && arrows.getItem() instanceof ItemArrow;

				if (!world.isRemote) {
					ItemArrow itemArrow = (ItemArrow) (arrows.getItem() instanceof ItemArrow ? arrows.getItem() : Items.arrow);
					EntityArrow entityArrow = itemArrow.makeTippedArrow(world, arrows, player);
					entityArrow.func_184547_a(player, player.rotationPitch, player.rotationYaw, 0.0F, arrowVelocity * 3.0F, 1.0F);

					if (arrowVelocity == 1.0f) {
						entityArrow.setIsCritical(true);
					}

					int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.power, bow);
					if (powerLevel > 0) {
						entityArrow.setDamage(entityArrow.getDamage() + (double) powerLevel * 0.5D + 0.5D);
					}

					int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.punch, bow);
					if (punchLevel > 0) {
						entityArrow.setKnockbackStrength(punchLevel);
					}

					if (EnchantmentHelper.getEnchantmentLevel(Enchantments.flame, bow) > 0) {
						entityArrow.setFire(100);
					}

					bow.damageItem(1, player);

					if (noAmmoConsumed) {
						entityArrow.canBePickedUp = EntityArrow.PickupStatus.CREATIVE_ONLY;
					}

					world.spawnEntityInWorld(entityArrow);
				}

				world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.entity_arrow_shoot, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + arrowVelocity * 0.5F);

				if (!noAmmoConsumed) {
					--arrows.stackSize;

					if (arrows.stackSize == 0) {
						player.inventory.deleteStack(arrows);
					}
				}

				player.addStat(StatList.func_188057_b(this));
			}
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		int charge = this.getMaxItemUseDuration(stack) - timeLeft;
		fireArrow(stack, worldIn, entityLiving, charge);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		return nockArrow(itemStackIn, worldIn, playerIn, hand);
	}
}
