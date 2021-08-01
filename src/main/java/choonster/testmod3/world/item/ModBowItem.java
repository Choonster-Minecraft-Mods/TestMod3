package choonster.testmod3.world.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * A bow that uses custom models identical to the vanilla ones and shoots custom arrows.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2576588-custom-bow-wont-load-model
 *
 * @author Choonster
 */
public class ModBowItem extends BowItem {
	public ModBowItem(final Item.Properties properties) {
		super(properties);
	}

	/**
	 * Is ammunition required to fire this bow?
	 *
	 * @param bow     The bow
	 * @param shooter The shooter
	 * @return Is ammunition required?
	 */
	protected boolean isAmmoRequired(final ItemStack bow, final Player shooter) {
		return !shooter.getAbilities().instabuild && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow) == 0;
	}

	/**
	 * Nock an arrow.
	 *
	 * @param bow     The bow ItemStack
	 * @param shooter The player shooting the bow
	 * @param world   The level
	 * @param hand    The hand holding the bow
	 * @return The result
	 */
	protected InteractionResultHolder<ItemStack> nockArrow(final ItemStack bow, final Level world, final Player shooter, final InteractionHand hand) {
		final boolean hasAmmo = !shooter.getProjectile(bow).isEmpty();

		final InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onArrowNock(bow, world, shooter, hand, hasAmmo);
		if (ret != null) return ret;

		if (isAmmoRequired(bow, shooter) && !hasAmmo) {
			return new InteractionResultHolder<>(InteractionResult.FAIL, bow);
		} else {
			shooter.startUsingItem(hand);
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, bow);
		}
	}

	/**
	 * Fire an arrow with the specified charge.
	 *
	 * @param bow     The bow ItemStack
	 * @param level   The firing player's level
	 * @param shooter The player firing the bow
	 * @param charge  The charge of the arrow
	 */
	void fireArrow(final ItemStack bow, final Level level, final LivingEntity shooter, final InteractionHand hand, int charge) {
		if (!(shooter instanceof final Player player)) return;

		final boolean ammoRequired = isAmmoRequired(bow, player);
		ItemStack ammo = player.getProjectile(bow);

		charge = ForgeEventFactory.onArrowLoose(bow, level, player, charge, !ammo.isEmpty() || !ammoRequired);
		if (charge < 0) return;

		if (!ammo.isEmpty() || !ammoRequired) {
			if (ammo.isEmpty()) {
				ammo = new ItemStack(Items.ARROW);
			}

			final float arrowVelocity = getPowerForTime(charge);

			if (arrowVelocity >= 0.1) {
				final boolean isInfinite = player.getAbilities().instabuild || (ammo.getItem() instanceof ArrowItem && ((ArrowItem) ammo.getItem()).isInfinite(ammo, bow, player));

				if (!level.isClientSide) {
					final ArrowItem arrowItem = (ArrowItem) (ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);

					AbstractArrow arrowEntity = arrowItem.createArrow(level, ammo, player);
					arrowEntity = customArrow(arrowEntity);
					arrowEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, arrowVelocity * 3.0f, 1.0f);

					if (arrowVelocity == 1.0f) {
						arrowEntity.setCritArrow(true);
					}

					final int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bow);
					if (powerLevel > 0) {
						arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() + (double) powerLevel * 0.5D + 0.5D);
					}

					final int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bow);
					if (punchLevel > 0) {
						arrowEntity.setKnockback(punchLevel);
					}

					if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bow) > 0) {
						arrowEntity.setSecondsOnFire(100);
					}

					bow.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(player.getUsedItemHand()));

					if (isInfinite) {
						arrowEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
					}

					level.addFreshEntity(arrowEntity);
				}

				level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0f, 1.0f / (level.random.nextFloat() * 0.4f + 1.2f) + arrowVelocity * 0.5f);

				if (!isInfinite && !player.getAbilities().instabuild) {
					ammo.shrink(1);
					if (ammo.isEmpty()) {
						player.getInventory().removeItem(ammo);
					}
				}

				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@Override
	public void releaseUsing(final ItemStack stack, final Level level, final LivingEntity livingEntity, final int timeLeft) {
		final int charge = stack.getUseDuration() - timeLeft;
		fireArrow(stack, level, livingEntity, livingEntity.getUsedItemHand(), charge);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level level, final Player playerIn, final InteractionHand hand) {
		return nockArrow(playerIn.getItemInHand(hand), level, playerIn, hand);
	}
}
