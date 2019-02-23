package choonster.testmod3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
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

	public ItemWithScripts(final IntFunction<String> scriptFunction) {
		this.scriptFunction = scriptFunction;
		setHasSubtypes(true);
	}

	private int getNumber(final ItemStack stack) {
		if (stack.hasTagCompound()) {
			return stack.getTagCompound().getInteger("Number");
		} else {
			return -1337;
		}
	}

	@Override
	public String getItemStackDisplayName(final ItemStack stack) {
		return super.getItemStackDisplayName(stack) + scriptFunction.apply(stack.getItemDamage());
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
