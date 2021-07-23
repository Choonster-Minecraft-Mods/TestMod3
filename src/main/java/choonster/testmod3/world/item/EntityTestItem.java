package choonster.testmod3.world.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
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
	public InteractionResultHolder<ItemStack> use(final Level world, final Player player, final InteractionHand hand) {
		final ItemStack heldItem = player.getItemInHand(hand);
		final HitResult rayTraceResult = getPlayerPOVHitResult(world, player, ClipContext.Fluid.ANY);
		if (rayTraceResult.getType() == HitResult.Type.MISS) {
			return new InteractionResultHolder<>(InteractionResult.PASS, heldItem);
		} else {
			final Vec3 lookVector = player.getViewVector(1.0f);
			final List<Entity> entitiesInWay = world.getEntities(player, player.getBoundingBox().expandTowards(lookVector.scale(5.0)).inflate(1.0), ENTITY_PREDICATE);
			if (!entitiesInWay.isEmpty()) {
				final Vec3 eyePosition = player.getEyePosition(1.0f);

				for (final Entity entity : entitiesInWay) {
					final AABB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
					if (aabb.contains(eyePosition)) {
						return new InteractionResultHolder<>(InteractionResult.PASS, heldItem);
					}
				}
			}

			if (rayTraceResult.getType() == HitResult.Type.BLOCK) {
				final Pig pig = EntityType.PIG.create(world);
				if (pig == null) {
					return new InteractionResultHolder<>(InteractionResult.FAIL, heldItem);
				}

				pig.setYRot(player.getYRot());

				if (!world.noCollision(pig, pig.getBoundingBox().inflate(-0.1))) {
					return new InteractionResultHolder<>(InteractionResult.FAIL, heldItem);
				} else {
					if (!world.isClientSide) {
						world.addFreshEntity(pig);
					}

					if (!player.getAbilities().instabuild) {
						heldItem.shrink(1);
					}

					player.awardStat(Stats.ITEM_USED.get(this));
					return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
				}
			} else {
				return new InteractionResultHolder<>(InteractionResult.PASS, heldItem);
			}
		}
	}
}
