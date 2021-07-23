package choonster.testmod3.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.function.Predicate;

/**
 * An Item that fires Snowballs at a fixed rate while right click is held
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,32389.0.html
 *
 * @author Choonster
 */
public class SnowballLauncherItem extends ShootableItem {

	/**
	 * The cooldown of the launcher (in ticks)
	 */
	private static final int COOLDOWN = 20;

	public SnowballLauncherItem(final Item.Properties properties) {
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
	private boolean isAmmoRequired(final ItemStack stack, final PlayerEntity player) {
		return !player.abilities.instabuild && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) == 0;
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return stack -> stack.getItem() == Items.SNOWBALL;
	}

	@Override
	public int getDefaultProjectileRange() {
		return 15;
	}

	@Override
	public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack heldItem = player.getItemInHand(hand);

		final boolean ammoRequired = isAmmoRequired(heldItem, player);
		final ItemStack ammo = player.getProjectile(heldItem);
		final boolean hasAmmo = !ammo.isEmpty();

		if (!ammoRequired || hasAmmo) {
			final int cooldown = getCooldown(heldItem);
			if (cooldown > 0) {
				player.getCooldowns().addCooldown(this, cooldown);
			}

			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (random.nextFloat() * 0.4f + 0.8f));

			if (!world.isClientSide) {
				final SnowballEntity entitySnowball = new SnowballEntity(world, player);
				entitySnowball.shootFromRotation(player, player.xRot, player.yRot, 0.0f, 1.5f, 1.0f);
				world.addFreshEntity(entitySnowball);
			}

			if (ammoRequired) {
				ammo.shrink(1);
				if (ammo.isEmpty()) {
					player.inventory.removeItem(ammo);
				}
			}

			return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
		}

		return new ActionResult<>(ActionResultType.FAIL, heldItem);
	}
}
