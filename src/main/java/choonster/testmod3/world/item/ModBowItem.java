package choonster.testmod3.world.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
	 * Nock an arrow.
	 *
	 * @param bow     The bow ItemStack
	 * @param shooter The player shooting the bow
	 * @param world   The level
	 * @param hand    The hand holding the bow
	 * @return The result
	 */
	protected InteractionResultHolder<ItemStack> nockArrow(final ItemStack bow, final Level world, final Player shooter, final InteractionHand hand) {
		final var hasAmmo = !shooter.getProjectile(bow).isEmpty();

		final var ret = ForgeEventFactory.onArrowNock(bow, world, shooter, hand, hasAmmo);
		if (ret != null) {
			return ret;
		}

		if (!shooter.hasInfiniteMaterials() && !hasAmmo) {
			return InteractionResultHolder.fail(bow);
		} else {
			shooter.startUsingItem(hand);
			return InteractionResultHolder.consume(bow);
		}
	}

	/**
	 * Fire one or more arrows with the specified charge.
	 *
	 * @param bow     The bow
	 * @param level   The firing player's level
	 * @param shooter The player firing the bow
	 * @param charge  The charge of the arrow
	 */
	protected void fireArrow(final ItemStack bow, final Level level, final LivingEntity shooter, int charge) {
		if (!(shooter instanceof final Player player)) {
			return;
		}

		final var projectile = shooter.getProjectile(bow);
		if (!projectile.isEmpty()) {
			charge = ForgeEventFactory.onArrowLoose(bow, level, player, charge, true);
			if (charge < 0) {
				return;
			}

			final var power = getPowerForTime(charge);
			if (power < 0.1) {
				return;
			}

			final var ammo = draw(bow, projectile, player);
			if (level instanceof final ServerLevel serverLevel && !ammo.isEmpty()) {
				shoot(
						serverLevel,
						player,
						player.getUsedItemHand(),
						bow,
						ammo,
						power * 3.0f,
						1.0f,
						power == 1.0f,
						null
				);
			}

			level.playSound(
					null,
					player.getX(),
					player.getY(),
					player.getZ(),
					SoundEvents.ARROW_SHOOT,
					SoundSource.PLAYERS,
					1.0f,
					1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + power * 0.5f
			);

			player.awardStat(Stats.ITEM_USED.get(this));
		}
	}

	@Override
	public void releaseUsing(final ItemStack stack, final Level level, final LivingEntity entity, final int timeLeft) {
		if (entity instanceof final Player player) {
			final var charge = getUseDuration(stack, entity) - timeLeft;
			fireArrow(stack, level, player, charge);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
		return nockArrow(player.getItemInHand(hand), level, player, hand);
	}
}
