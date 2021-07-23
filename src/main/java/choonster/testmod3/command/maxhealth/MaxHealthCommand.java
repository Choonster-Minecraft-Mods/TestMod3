package choonster.testmod3.command.maxhealth;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.MaxHealthCapability;
import choonster.testmod3.text.TestMod3Lang;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * Base class for commands that affect an entity's {@link IMaxHealth}.
 *
 * @author Choonster
 */
public class MaxHealthCommand {
	private static final SimpleCommandExceptionType INVALID_ENTITY_EXCEPTION = new SimpleCommandExceptionType(
			new TranslatableComponent(TestMod3Lang.COMMAND_MAX_HEALTH_INVALID_ENTITY.getTranslationKey())
	);

	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		return Commands.literal("maxhealth")
				.then(AddMaxHealthCommand.register())
				.then(GetMaxHealthCommand.register())
				.then(SetMaxHealthCommand.register());
	}

	static ArgumentBuilder<CommandSourceStack, ?> create(final ArgumentBuilder<CommandSourceStack, ?> builder, final IEntityProcessor processor, final String successMessage) {
		return builder
				.then(Commands.argument("entity", EntityArgument.entity())
						.then(Commands.argument("amount", FloatArgumentType.floatArg())
								.executes(context ->
										execute(
												context,
												EntityArgument.getEntity(context, "entity"),
												FloatArgumentType.getFloat(context, "amount"),
												processor,
												successMessage
										))

						)
				);
	}

	/**
	 * Executes the command.
	 *
	 * @param context        The command context
	 * @param entity         The specified entity
	 * @param amount         The specified max health amount
	 * @param processor      The entity processor
	 * @param successMessage The translation key of the message to send when the command succeeds.
	 *                       This will be provided with the entity's display name and the amount as format arguments.
	 */
	private static int execute(final CommandContext<CommandSourceStack> context, final Entity entity, final float amount, final IEntityProcessor processor, final String successMessage) throws CommandSyntaxException {
		if (!(entity instanceof LivingEntity)) {
			throw INVALID_ENTITY_EXCEPTION.create();
		}

		final LivingEntity entityLivingBase = (LivingEntity) entity;

		MaxHealthCapability.getMaxHealth(entityLivingBase)
				.ifPresent(maxHealth -> processor.process(entityLivingBase, maxHealth, amount));

		context.getSource()
				.sendSuccess(new TranslatableComponent(successMessage, entity.getDisplayName(), MaxHealthCapability.formatMaxHealth(amount)), true);

		return 0;
	}

	@FunctionalInterface
	interface IEntityProcessor {
		/**
		 * Make a change to the entity's {@link IMaxHealth}.
		 *
		 * @param entity    The entity
		 * @param maxHealth The entity's IMaxHealth
		 * @param amount    The amount to add/set
		 */
		void process(LivingEntity entity, IMaxHealth maxHealth, float amount);
	}
}
