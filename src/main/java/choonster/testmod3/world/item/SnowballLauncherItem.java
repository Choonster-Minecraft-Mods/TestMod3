package choonster.testmod3.world.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

/**
 * An Item that fires Snowballs at a fixed rate while right click is held
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,32389.0.html
 *
 * @author Choonster
 */
public class SnowballLauncherItem extends ProjectileWeaponItem {

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
	private boolean isAmmoRequired(final ItemStack stack, final Player player) {
		return !player.getAbilities().instabuild && stack.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) == 0;
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
	public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
		final ItemStack heldItem = player.getItemInHand(hand);

		final boolean ammoRequired = isAmmoRequired(heldItem, player);
		final ItemStack ammo = player.getProjectile(heldItem);
		final boolean hasAmmo = !ammo.isEmpty();

		if (!ammoRequired || hasAmmo) {
			final int cooldown = getCooldown(heldItem);
			if (cooldown > 0) {
				player.getCooldowns().addCooldown(this, cooldown);
			}

			level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (level.random.nextFloat() * 0.4f + 0.8f));

			if (!level.isClientSide) {
				final Snowball entitySnowball = new Snowball(level, player);
				entitySnowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.5f, 1.0f);
				level.addFreshEntity(entitySnowball);
			}

			if (ammoRequired) {
				ammo.shrink(1);
				if (ammo.isEmpty()) {
					player.getInventory().removeItem(ammo);
				}
			}

			return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
		}

		return new InteractionResultHolder<>(InteractionResult.FAIL, heldItem);
	}
}
