package choonster.testmod3.world.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import java.util.function.Predicate;

/**
 * An item that places a Pig where the player is looking when used. Based on {@link BoatItem}.
 *
 * @author Choonster
 */
public class EntityTestItem extends Item {
	private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

	public EntityTestItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		final var heldItem = player.getItemInHand(hand);
		final var hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);

		if (hitResult.getType() == HitResult.Type.MISS) {
			return InteractionResult.PASS;
		}

		final var lookVector = player.getViewVector(1.0f);
		final var entitiesInWay = level.getEntities(player, player.getBoundingBox().expandTowards(lookVector.scale(5.0)).inflate(1.0), ENTITY_PREDICATE);
		if (!entitiesInWay.isEmpty()) {
			final var eyePosition = player.getEyePosition(1.0f);

			for (final var entity : entitiesInWay) {
				final var aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
				if (aabb.contains(eyePosition)) {
					return InteractionResult.PASS;
				}
			}
		}

		if (hitResult.getType() != HitResult.Type.BLOCK) {
			return InteractionResult.PASS;
		}

		final var pig = EntityType.PIG.create(level, EntitySpawnReason.SPAWN_ITEM_USE);
		if (pig == null) {
			return InteractionResult.FAIL;
		}

		pig.setYRot(player.getYRot());

		if (!level.noCollision(pig, pig.getBoundingBox().inflate(-0.1))) {
			return InteractionResult.FAIL;
		}

		if (!level.isClientSide()) {
			level.addFreshEntity(pig);
		}

		if (!player.getAbilities().instabuild) {
			heldItem.shrink(1);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResult.SUCCESS;
	}
}
