package choonster.testmod3.command.maxhealth;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.CapabilityMaxHealth;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Get the current max health of an entity and the bonus max health provided by its {@link IMaxHealth}.
 *
 * @author Choonster
 */
public class GetMaxHealthCommand {
	private static final SimpleCommandExceptionType INVALID_ENTITY_EXCEPTION = new SimpleCommandExceptionType(new TextComponentTranslation("commands.testmod3.maxhealth.invalid_entity"));

	static ArgumentBuilder<CommandSource, ?> register() {
		return Commands.literal("get")
				.then(Commands.argument("entity", EntityArgument.entity()))
				.executes(context -> execute(
						context,
						EntityArgument.getEntity(context, "entity")
				));
	}

	private static int execute(final CommandContext<CommandSource> context, final Entity entity) throws CommandSyntaxException {
		if (!(entity instanceof EntityLivingBase)) {
			throw INVALID_ENTITY_EXCEPTION.create();
		}

		final EntityLivingBase entityLivingBase = (EntityLivingBase) entity;

		CapabilityMaxHealth.getMaxHealth(entityLivingBase).ifPresent(maxHealth ->
				context.getSource().sendFeedback(
						new TextComponentTranslation(
								"message.testmod3:max_health.get",
								entity.getDisplayName(),
								CapabilityMaxHealth.formatMaxHealth(maxHealth.getBonusMaxHealth())
						),
						true
				)
		);

		return 0;
	}
}
