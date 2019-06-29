package choonster.testmod3.command.maxhealth;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.MaxHealthCapability;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Get the current max health of an entity and the bonus max health provided by its {@link IMaxHealth}.
 *
 * @author Choonster
 */
public class GetMaxHealthCommand {
	private static final SimpleCommandExceptionType INVALID_ENTITY_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent("commands.testmod3.maxhealth.invalid_entity"));

	static ArgumentBuilder<CommandSource, ?> register() {
		return Commands.literal("get")
				.then(Commands.argument("entity", EntityArgument.entity()))
				.executes(context -> execute(
						context,
						EntityArgument.getEntity(context, "entity")
				));
	}

	private static int execute(final CommandContext<CommandSource> context, final Entity entity) throws CommandSyntaxException {
		if (!(entity instanceof LivingEntity)) {
			throw INVALID_ENTITY_EXCEPTION.create();
		}

		final LivingEntity entityLivingBase = (LivingEntity) entity;

		MaxHealthCapability.getMaxHealth(entityLivingBase).ifPresent(maxHealth ->
				context.getSource().sendFeedback(
						new TranslationTextComponent(
								"message.testmod3.max_health.get",
								entity.getDisplayName(),
								MaxHealthCapability.formatMaxHealth(maxHealth.getBonusMaxHealth())
						),
						true
				)
		);

		return 0;
	}
}
