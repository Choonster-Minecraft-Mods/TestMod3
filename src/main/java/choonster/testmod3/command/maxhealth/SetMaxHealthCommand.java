package choonster.testmod3.command.maxhealth;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.EntityLivingBase;

/**
 * Set the max health provided by an entity's {@link IMaxHealth}.
 *
 * @author Choonster
 */
class SetMaxHealthCommand {
	static ArgumentBuilder<CommandSource, ?> register() {
		return MaxHealthCommand.create(
				Commands.literal("set"),
				SetMaxHealthCommand::processEntity,
				"message.testmod3.max_health.set"
		);
	}

	private static void processEntity(final EntityLivingBase entity, final IMaxHealth maxHealth, final float amount) {
		maxHealth.setBonusMaxHealth(amount);
	}
}
