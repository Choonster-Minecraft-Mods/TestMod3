package choonster.testmod3.entity;

import choonster.testmod3.init.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * A creeper that avoids players holding an item in their off hand.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2782263-how-to-make-a-creeper-avoid-certain-players
 *
 * @author Choonster
 */
public class PlayerAvoidingCreeperEntity extends CreeperEntity {

	public PlayerAvoidingCreeperEntity(final EntityType<? extends CreeperEntity> p_i50213_1_, final World p_i50213_2_) {
		super(p_i50213_1_, p_i50213_2_);
	}

	@Override
	public EntityType<?> getType() {
		return ModEntities.PLAYER_AVOIDING_CREEPER;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

		// Remove the EntityAINearestAttackableTarget task added by EntityCreeper so it can be replaced
		// TODO: Check if the replacement NearestAttackableTargetGoal overrides this
//		targetSelector.taskEntries.stream()
//				.filter(taskEntry -> taskEntry.action instanceof NearestAttackableTargetGoal)
//				.findFirst()
//				.ifPresent(taskEntry -> targetTasks.removeTask(taskEntry.action));

		// Avoid players if they have an item in their off hand
		goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PlayerEntity.class, 6.0F, 1.0D, 1.2D, this::shouldAvoidEntity));

		// Only attack players without an item in their off hand
		targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, (player) -> !shouldAvoidEntity(player)));
	}

	/**
	 * Should this creeper avoid the specified entity?
	 *
	 * @param entity The entity
	 * @return True if the entity is a player with an item in their off hand
	 */
	private boolean shouldAvoidEntity(@Nullable final Entity entity) {
		return entity instanceof PlayerEntity && !((PlayerEntity) entity).getHeldItemOffhand().isEmpty();
	}
}
