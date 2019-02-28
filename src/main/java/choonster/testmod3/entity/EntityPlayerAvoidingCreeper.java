package choonster.testmod3.entity;

import choonster.testmod3.init.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
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
public class EntityPlayerAvoidingCreeper extends EntityCreeper {
	public EntityPlayerAvoidingCreeper(final World worldIn) {
		super(worldIn);
	}

	@Override
	public EntityType<?> getType() {
		return ModEntities.PLAYER_AVOIDING_CREEPER;
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();

		// Remove the EntityAINearestAttackableTarget task added by EntityCreeper so it can be replaced
		targetTasks.taskEntries.stream()
				.filter(taskEntry -> taskEntry.action instanceof EntityAINearestAttackableTarget)
				.findFirst()
				.ifPresent(taskEntry -> targetTasks.removeTask(taskEntry.action));

		// Avoid players if they have an item in their off hand
		tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityPlayer.class, 6.0F, 1.0D, 1.2D, this::shouldAvoidEntity));

		// Only attack players without an item in their off hand
		targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, 10, true, false, (player) -> !shouldAvoidEntity(player)));
	}

	/**
	 * Should this creeper avoid the specified entity?
	 *
	 * @param entity The entity
	 * @return True if the entity is a player with an item in their off hand
	 */
	private boolean shouldAvoidEntity(@Nullable final Entity entity) {
		return entity instanceof EntityPlayer && !((EntityPlayer) entity).getHeldItemOffhand().isEmpty();
	}
}
