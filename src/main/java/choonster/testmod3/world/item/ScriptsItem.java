package choonster.testmod3.world.item;

import choonster.testmod3.init.ModDataComponents;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.IntFunction;

/**
 * An item that displays a number stored in a component in its display name.
 *
 * @author Choonster
 */
public abstract class ScriptsItem extends Item {
	private final IntFunction<String> scriptFunction;

	public ScriptsItem(final IntFunction<String> scriptFunction, final Properties properties) {
		super(properties);
		this.scriptFunction = scriptFunction;
	}

	private static int getNumber(final ItemStack stack) {
		return stack.getOrDefault(ModDataComponents.SCRIPTS_NUMBER.get(), -1337);
	}

	@Override
	public Component getName(final ItemStack stack) {
		return super.getName(stack).copy().append(scriptFunction.apply(getNumber(stack)));
	}

	@Override
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		final ItemStack heldItem = player.getItemInHand(hand);

		if (!level.isClientSide() && player instanceof final ServerPlayer serverPlayer) {
			serverPlayer.sendSystemMessage(
					Component.translatable(
							String.format(
									TestMod3Lang.MESSAGE_SCRIPTS_RIGHT_CLICK.getTranslationKey(),
									descriptionId
							),
							scriptFunction.apply(getNumber(heldItem))
					)
			);
		}

		return InteractionResult.SUCCESS;
	}
}
