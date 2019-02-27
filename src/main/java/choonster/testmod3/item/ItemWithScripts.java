package choonster.testmod3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.function.IntFunction;

/**
 * An item that displays a number stored in NBT in its display name.
 *
 * @author Choonster
 */
public abstract class ItemWithScripts extends Item {
	private final IntFunction<String> scriptFunction;

	public ItemWithScripts(final IntFunction<String> scriptFunction, final Properties properties) {
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
		return super.getDisplayName(stack).appendText(scriptFunction.apply(getNumber(stack)));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand hand) {
		final ItemStack heldItem = playerIn.getHeldItem(hand);

		if (!worldIn.isRemote) {
			playerIn.sendMessage(new TextComponentTranslation("message." + getRegistryName() + ".right_click", scriptFunction.apply(getNumber(heldItem))));
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
	}
}
