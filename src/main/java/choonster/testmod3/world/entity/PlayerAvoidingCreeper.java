package choonster.testmod3.world.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

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
public class PlayerAvoidingCreeper extends Creeper {
	private static final Field AVAILABLE_GOALS = ObfuscationReflectionHelper.findField(GoalSelector.class, /* availableGoals */ "f_25345_");

	public PlayerAvoidingCreeper(final EntityType<? extends Creeper> entityType, final Level world) {
		super(entityType, world);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Creeper.createAttributes();
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

		final Set<WrappedGoal> targetSelectorGoals;

		try {
			@SuppressWarnings("unchecked")
			final Set<WrappedGoal> availableGoals = (Set<WrappedGoal>) AVAILABLE_GOALS.get(targetSelector);
			targetSelectorGoals = availableGoals;
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Unable to access target goals", e);
		}

		// Remove the NearestAttackableTargetGoal added by CreeperEntity, so it can be replaced
		targetSelectorGoals
				.stream()
				.map(WrappedGoal::getGoal)
				.filter(goal -> goal instanceof NearestAttackableTargetGoal)
				.findFirst()
				.ifPresent(targetSelector::removeGoal);

		// Avoid players if they have an item in their off hand
		goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.0D, 1.2D, PlayerAvoidingCreeper::shouldAvoidEntity));

		// Only attack players without an item in their off hand
		targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (player) -> !shouldAvoidEntity(player)));
	}

	/**
	 * Should this creeper avoid the specified entity?
	 *
	 * @param entity The entity
	 * @return True if the entity is a player with an item in their off hand
	 */
	private static boolean shouldAvoidEntity(@Nullable final Entity entity) {
		return entity instanceof Player && !((Player) entity).getOffhandItem().isEmpty();
	}
}
