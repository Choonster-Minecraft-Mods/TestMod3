package choonster.testmod3.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

/**
 * An Item that fires Snowballs at a fixed rate while right click is held
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,32389.0.html
 *
 * @author Choonster
 */
public class ItemSnowballLauncher extends Item {

	/**
	 * The cooldown of the launcher (in ticks)
	 */
	private static final int COOLDOWN = 20;

	public ItemSnowballLauncher(final Item.Properties properties) {
		super(properties);
	}

	/**
	 * Get the cooldown of the launcher (in ticks).
	 *
	 * @param launcher The launcher
	 * @return The cooldown of the launcher (in ticks), or 0 if there is none
	 */
	protected int getCooldown(final ItemStack launcher) {
		return COOLDOWN;
	}

	/**
	 * Does the player need ammunition to fire the launcher?
	 *
	 * @param stack  The launcher ItemStack
	 * @param player The player to check
	 * @return True if the player is not in creative mode and the launcher doesn't have the Infinity enchantment
	 */
	private boolean isAmmoRequired(final ItemStack stack, final EntityPlayer player) {
		return !player.abilities.isCreativeMode && EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) == 0;
	}

	/**
	 * Is the {@link ItemStack} valid ammunition?
	 *
	 * @param stack The ItemStack
	 * @return Is the ItemStack valid ammunition?
	 */
	protected boolean isAmmo(final ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() == Items.SNOWBALL;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand hand) {
		final ItemStack heldItem = playerIn.getHeldItem(hand);

		final boolean ammoRequired = isAmmoRequired(heldItem, playerIn);
		final LazyOptional<IItemHandler> optionalAmmoSlot = ammoRequired ? ItemModBow.findAmmoSlot(playerIn, this::isAmmo) : LazyOptional.empty();
		final boolean hasAmmo = optionalAmmoSlot.isPresent();

		if (!ammoRequired || hasAmmo) {
			final int cooldown = getCooldown(heldItem);
			if (cooldown > 0) {
				playerIn.getCooldownTracker().setCooldown(this, cooldown);
			}

			worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

			if (!worldIn.isRemote) {
				final EntitySnowball entitySnowball = new EntitySnowball(worldIn, playerIn);
				entitySnowball.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
				worldIn.spawnEntity(entitySnowball);
			}

			optionalAmmoSlot.ifPresent(ammoSlot -> {
				if (!ammoSlot.extractItem(0, 1, true).isEmpty()) {
					ammoSlot.extractItem(0, 1, false);
					playerIn.inventoryContainer.detectAndSendChanges();
				}
			});

			return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
		}

		return new ActionResult<>(EnumActionResult.FAIL, heldItem);
	}
}
