package choonster.testmod3.command.maxhealth;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.LivingEntity;

/**
 * Add max health to an entity using {@link IMaxHealth}.
 *
 * @author Choonster
 */
class AddMaxHealthCommand {
	static ArgumentBuilder<CommandSourceStack, ?> register() {
		return MaxHealthCommand.create(
				Commands.literal("add"),
				AddMaxHealthCommand::addMaxHealth,
				"commands.testmod3.add_max_health.usage"
		);
	}

	private static void addMaxHealth(final LivingEntity entity, final IMaxHealth maxHealth, final float amount) {
		maxHealth.addBonusMaxHealth(amount);
	}
}
