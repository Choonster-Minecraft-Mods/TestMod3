package choonster.testmod3.item;

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

	private int getNumber(final ItemStack stack) {
		if (stack.hasTag()) {
			return stack.getTag().getInt("Number");
		} else {
			return -1337;
		}
	}

	@Override
	public ITextComponent getDisplayName(final ItemStack stack) {
		return super.getDisplayName(stack).deepCopy().appendString(scriptFunction.apply(getNumber(stack)));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final PlayerEntity playerIn, final Hand hand) {
		final ItemStack heldItem = playerIn.getHeldItem(hand);

		if (!worldIn.isRemote) {
			playerIn.sendMessage(new TranslationTextComponent("message." + getRegistryName() + ".right_click", scriptFunction.apply(getNumber(heldItem))), Util.DUMMY_UUID);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
	}
}
