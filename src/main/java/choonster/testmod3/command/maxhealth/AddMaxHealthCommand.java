package choonster.testmod3.command.maxhealth;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.EntityLivingBase;

/**
 * Add max health to an entity using {@link IMaxHealth}.
 *
 * @author Choonster
 */
class AddMaxHealthCommand {
	static ArgumentBuilder<CommandSource, ?> register() {
		return MaxHealthCommand.create(
				Commands.literal("add"),
				AddMaxHealthCommand::addMaxHealth,
				"commands.testmod3.add_max_health.usage"
		);
	}

	private static void addMaxHealth(final EntityLivingBase entity, final IMaxHealth maxHealth, final float amount) {
		maxHealth.addBonusMaxHealth(amount);
	}
}
