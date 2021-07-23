package choonster.testmod3.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoatItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

/**
 * An item that places a Pig where the player is looking when used. Based on {@link BoatItem}.
 *
 * @author Choonster
 */
public class EntityTestItem extends Item {
	private static final Predicate<Entity> ENTITY_PREDICATE = EntityPredicates.NO_SPECTATORS.and(Entity::isPickable);

	public EntityTestItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack heldItem = player.getItemInHand(hand);
		final RayTraceResult rayTraceResult = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.ANY);
		if (rayTraceResult.getType() == RayTraceResult.Type.MISS) {
			return new ActionResult<>(ActionResultType.PASS, heldItem);
		} else {
			final Vector3d lookVector = player.getViewVector(1.0f);
			final List<Entity> entitiesInWay = world.getEntities(player, player.getBoundingBox().expandTowards(lookVector.scale(5.0)).inflate(1.0), ENTITY_PREDICATE);
			if (!entitiesInWay.isEmpty()) {
				final Vector3d eyePosition = player.getEyePosition(1.0f);

				for (final Entity entity : entitiesInWay) {
					final AxisAlignedBB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
					if (aabb.contains(eyePosition)) {
						return new ActionResult<>(ActionResultType.PASS, heldItem);
					}
				}
			}

			if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
				final PigEntity pig = EntityType.PIG.create(world);
				if (pig == null) {
					return new ActionResult<>(ActionResultType.FAIL, heldItem);
				}

				pig.yRot = player.yRot;

				if (!world.noCollision(pig, pig.getBoundingBox().inflate(-0.1))) {
					return new ActionResult<>(ActionResultType.FAIL, heldItem);
				} else {
					if (!world.isClientSide) {
						world.addFreshEntity(pig);
					}

					if (!player.abilities.instabuild) {
						heldItem.shrink(1);
					}

					player.awardStat(Stats.ITEM_USED.get(this));
					return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
				}
			} else {
				return new ActionResult<>(ActionResultType.PASS, heldItem);
			}
		}
	}
}
