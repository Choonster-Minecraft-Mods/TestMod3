package choonster.testmod3.world.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return stack -> stack.getItem() == Items.SNOWBALL;
	}

	@Override
	public int getDefaultProjectileRange() {
		return 15;
	}

	@Override
	protected Projectile createProjectile(
			final Level level,
			final LivingEntity shooter,
			final ItemStack projectile,
			final ItemStack weapon,
			final boolean isFullPower
	) {
		return new Snowball(level, shooter, projectile);
	}

	@Override
	protected void shootProjectile(
			final LivingEntity shooter,
			final Projectile projectile,
			final int projectileNumber,
			final float p_335337_,
			final float p_332934_,
			final float yRot,
			@Nullable final LivingEntity target
	) {
		projectile.shootFromRotation(
				shooter,
				shooter.getXRot(),
				shooter.getYRot() + yRot,
				0.0F,
				p_335337_,
				p_332934_
		);
	}

	@Override
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		final var heldItem = player.getItemInHand(hand);

		final var projectile = player.getProjectile(heldItem);
		final var hasAmmo = !projectile.isEmpty();

		if (!player.hasInfiniteMaterials() && !hasAmmo) {
			return InteractionResult.FAIL;
		}

		final var cooldown = getCooldown(heldItem);
		if (cooldown > 0) {
			player.getCooldowns().addCooldown(heldItem, cooldown);
		}

		final var ammo = draw(heldItem, projectile, player);
		if (level instanceof final ServerLevel serverLevel && !ammo.isEmpty()) {
			shoot(
					serverLevel,
					player,
					player.getUsedItemHand(),
					heldItem,
					ammo,
					3.0f,
					1.0f,
					true,
					null
			);
		}

		level.playSound(
				null,
				player.getX(),
				player.getY(),
				player.getZ(),
				SoundEvents.SNOWBALL_THROW,
				SoundSource.NEUTRAL,
				0.5f,
				1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + 0.5f
		);

		return InteractionResult.CONSUME;
	}
}
