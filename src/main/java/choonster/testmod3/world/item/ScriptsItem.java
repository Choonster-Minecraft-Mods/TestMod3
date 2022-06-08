package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.IntFunction;

/**
 * An item that displays a number stored in NBT in its display name.
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
		if (stack.hasTag()) {
			return stack.getTag().getInt("Number");
		} else {
			return -1337;
		}
	}

	@Override
	public Component getName(final ItemStack stack) {
		return super.getName(stack).copy().append(scriptFunction.apply(getNumber(stack)));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level level, final Player playerIn, final InteractionHand hand) {
		final ItemStack heldItem = playerIn.getItemInHand(hand);

		if (!level.isClientSide) {
			playerIn.sendSystemMessage(Component.translatable(String.format(TestMod3Lang.MESSAGE_SCRIPTS_RIGHT_CLICK.getTranslationKey(), getDescriptionId()), scriptFunction.apply(getNumber(heldItem))));
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
	}
}
