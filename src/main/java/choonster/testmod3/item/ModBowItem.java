package choonster.testmod3.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;
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
	protected boolean isAmmoRequired(final ItemStack bow, final PlayerEntity shooter) {
		return !shooter.abilities.instabuild && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow) == 0;
	}

	/**
	 * Nock an arrow.
	 *
	 * @param bow     The bow ItemStack
	 * @param shooter The player shooting the bow
	 * @param world   The World
	 * @param hand    The hand holding the bow
	 * @return The result
	 */
	protected ActionResult<ItemStack> nockArrow(final ItemStack bow, final World world, final PlayerEntity shooter, final Hand hand) {
		final boolean hasAmmo = !shooter.getProjectile(bow).isEmpty();

		final ActionResult<ItemStack> ret = ForgeEventFactory.onArrowNock(bow, world, shooter, hand, hasAmmo);
		if (ret != null) return ret;

		if (isAmmoRequired(bow, shooter) && !hasAmmo) {
			return new ActionResult<>(ActionResultType.FAIL, bow);
		} else {
			shooter.startUsingItem(hand);
			return new ActionResult<>(ActionResultType.SUCCESS, bow);
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
	void fireArrow(final ItemStack bow, final World world, final LivingEntity shooter, final Hand hand, int charge) {
		if (!(shooter instanceof PlayerEntity)) return;

		final PlayerEntity player = (PlayerEntity) shooter;
		final boolean ammoRequired = isAmmoRequired(bow, player);
		ItemStack ammo = player.getProjectile(bow);

		charge = ForgeEventFactory.onArrowLoose(bow, world, player, charge, !ammo.isEmpty() || !ammoRequired);
		if (charge < 0) return;

		if (!ammo.isEmpty() || !ammoRequired) {
			if (ammo.isEmpty()) {
				ammo = new ItemStack(Items.ARROW);
			}

			final float arrowVelocity = getPowerForTime(charge);

			if (arrowVelocity >= 0.1) {
				final boolean isInfinite = player.abilities.instabuild || (ammo.getItem() instanceof ArrowItem && ((ArrowItem) ammo.getItem()).isInfinite(ammo, bow, player));

				if (!world.isClientSide) {
					final ArrowItem arrowItem = (ArrowItem) (ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);

					AbstractArrowEntity arrowEntity = arrowItem.createArrow(world, ammo, player);
					arrowEntity = customArrow(arrowEntity);
					arrowEntity.shootFromRotation(player, player.xRot, player.yRot, 0.0f, arrowVelocity * 3.0f, 1.0f);

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
						arrowEntity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
					}

					world.addFreshEntity(arrowEntity);
				}

				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (random.nextFloat() * 0.4f + 1.2f) + arrowVelocity * 0.5f);

				if (!isInfinite && !player.abilities.instabuild) {
					ammo.shrink(1);
					if (ammo.isEmpty()) {
						player.inventory.removeItem(ammo);
					}
				}

				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@Override
	public void releaseUsing(final ItemStack stack, final World worldIn, final LivingEntity livingEntity, final int timeLeft) {
		final int charge = stack.getUseDuration() - timeLeft;
		fireArrow(stack, worldIn, livingEntity, livingEntity.getUsedItemHand(), charge);
	}

	@Override
	public ActionResult<ItemStack> use(final World worldIn, final PlayerEntity playerIn, final Hand hand) {
		return nockArrow(playerIn.getItemInHand(hand), worldIn, playerIn, hand);
	}
}
