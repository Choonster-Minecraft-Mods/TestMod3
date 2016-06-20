package choonster.testmod3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.function.Function;

/**
 * An item that displays a number stored in NBT in its display name.
 *
 * @author Choonster
 */
public abstract class ItemWithScripts extends ItemTestMod3 {
	private final Function<Integer, String> scriptFunction;

	public ItemWithScripts(Function<Integer, String> scriptFunction, String itemName) {
		super(itemName);
		this.scriptFunction = scriptFunction;
		setHasSubtypes(true);
	}

	private int getNumber(ItemStack stack) {
		if (stack.hasTagCompound()) {
			return stack.getTagCompound().getInteger("Number");
		} else {
			return -1337;
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return super.getItemStackDisplayName(stack) + scriptFunction.apply(stack.getItemDamage());
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (!worldIn.isRemote) {
			playerIn.addChatComponentMessage(new TextComponentTranslation("message." + getRegistryName() + ".rightClick", scriptFunction.apply(getNumber(itemStackIn))));
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}
}
