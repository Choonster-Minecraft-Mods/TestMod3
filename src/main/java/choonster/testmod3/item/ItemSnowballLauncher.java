package choonster.testmod3.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

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
	 * The cooldown of the launcher (in ticks)
	 */
	private static final int COOLDOWN = 20;

	public ItemSnowballLauncher(String itemName) {
		super(itemName);
	}

	/**
	 * Get the cooldown of the launcher (in ticks).
	 *
	 * @param launcher The launcher
	 * @return The cooldown of the launcher (in ticks), or 0 if there is none
	 */
	protected int getCooldown(ItemStack launcher) {
		return COOLDOWN;
	}

	/**
	 * Does the player need ammunition to fire the launcher?
	 *
	 * @param stack  The launcher ItemStack
	 * @param player The player to check
	 * @return True if the player is not in creative mode and the launcher doesn't have the Infinity enchantment
	 */
	private boolean isAmmoRequired(ItemStack stack, EntityPlayer player) {
		return !player.capabilities.isCreativeMode && EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) == 0;
	}

	/**
	 * Is the {@link ItemStack} valid ammunition?
	 *
	 * @param stack The ItemStack
	 * @return Is the ItemStack valid ammunition?
	 */
	protected boolean isAmmo(@Nullable ItemStack stack) {
		return stack != null && stack.getItem() == Items.SNOWBALL;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		final boolean ammoRequired = isAmmoRequired(itemStackIn, playerIn);
		final IItemHandler ammoSlot = ammoRequired ? ItemModBow.findAmmoSlot(playerIn, this::isAmmo) : null;
		final boolean hasAmmo = ammoSlot != null;

		if (!ammoRequired || hasAmmo) {
			final int cooldown = getCooldown(itemStackIn);
			if (cooldown > 0) {
				playerIn.getCooldownTracker().setCooldown(this, cooldown);
			}

			worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			if (!worldIn.isRemote) {
				final EntitySnowball entitySnowball = new EntitySnowball(worldIn, playerIn);
				entitySnowball.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
				worldIn.spawnEntityInWorld(entitySnowball);
			}

			if (hasAmmo && ammoSlot.extractItem(0, 1, true) != null) {
				ammoSlot.extractItem(0, 1, false);
				playerIn.inventoryContainer.detectAndSendChanges();
			}

			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
		}

		return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
	}
}
