package choonster.testmod3.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

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
	public ITextComponent getName(final ItemStack stack) {
		return super.getName(stack).copy().append(scriptFunction.apply(getNumber(stack)));
	}

	@Override
	public ActionResult<ItemStack> use(final World worldIn, final PlayerEntity playerIn, final Hand hand) {
		final ItemStack heldItem = playerIn.getItemInHand(hand);

		if (!worldIn.isClientSide) {
			playerIn.sendMessage(new TranslationTextComponent(String.format(TestMod3Lang.MESSAGE_SCRIPTS_RIGHT_CLICK.getTranslationKey(), getDescriptionId()), scriptFunction.apply(getNumber(heldItem))), Util.NIL_UUID);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
	}
}
