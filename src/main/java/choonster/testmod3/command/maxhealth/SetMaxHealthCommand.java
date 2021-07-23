package choonster.testmod3.command.maxhealth;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.text.TestMod3Lang;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.LivingEntity;

/**
 * Set the max health provided by an entity's {@link IMaxHealth}.
 *
 * @author Choonster
 */
class SetMaxHealthCommand {
	static ArgumentBuilder<CommandSourceStack, ?> register() {
		return MaxHealthCommand.create(
				Commands.literal("set"),
				SetMaxHealthCommand::processEntity,
				TestMod3Lang.MESSAGE_MAX_HEALTH_SET.getTranslationKey()
		);
	}

	private static void processEntity(final LivingEntity entity, final IMaxHealth maxHealth, final float amount) {
		maxHealth.setBonusMaxHealth(amount);
	}
}
