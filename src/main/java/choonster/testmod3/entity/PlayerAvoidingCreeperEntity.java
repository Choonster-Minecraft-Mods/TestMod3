package choonster.testmod3.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * A creeper that avoids players holding an item in their off hand.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2782263-how-to-make-a-creeper-avoid-certain-players
 *
 * @author Choonster
 */
public class PlayerAvoidingCreeperEntity extends CreeperEntity {
	private static final Field GOALS = ObfuscationReflectionHelper.findField(GoalSelector.class, /* goals */ "field_220892_d");

	public PlayerAvoidingCreeperEntity(final EntityType<? extends CreeperEntity> entityType, final World world) {
		super(entityType, world);
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return CreeperEntity.registerAttributes();
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

		final Set<PrioritizedGoal> targetSelectorGoals;

		try {
			@SuppressWarnings("unchecked")
			final Set<PrioritizedGoal> goals = (Set<PrioritizedGoal>) GOALS.get(targetSelector);
			targetSelectorGoals = goals;
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Unable to access target goals", e);
		}

		// Remove the NearestAttackableTargetGoal added by CreeperEntity so it can be replaced
		targetSelectorGoals
				.stream()
				.map(PrioritizedGoal::getGoal)
				.filter(goal -> goal instanceof NearestAttackableTargetGoal)
				.findFirst()
				.ifPresent(targetSelector::removeGoal);

		// Avoid players if they have an item in their off hand
		goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PlayerEntity.class, 6.0F, 1.0D, 1.2D, this::shouldAvoidEntity));

		// Only attack players without an item in their off hand
		targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, (player) -> !shouldAvoidEntity(player)));
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
