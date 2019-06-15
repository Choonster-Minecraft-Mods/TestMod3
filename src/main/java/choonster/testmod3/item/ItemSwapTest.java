package choonster.testmod3.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * An item that's converted to another when shift-right clicked.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34244.0.html
 *
 * @author Choonster
 */
public class ItemSwapTest extends Item {
	private ItemStack otherItem;

	public ItemSwapTest(final Item.Properties properties) {
		super(properties);
	}

	public boolean hasOtherItem() {
		return otherItem != null;
	}

	public void setOtherItem(final ItemStack otherItem) {
		this.otherItem = otherItem;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);

		if (hasOtherItem()) {
			tooltip.add(new TextComponentTranslation("item.testmod3.swap_test.with_item.desc", otherItem.getDisplayName()));
		} else {
			tooltip.add(new TextComponentTranslation("item.testmod3.swap_test.without_item.desc"));
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand hand) {
		if (hasOtherItem() && playerIn.isSneaking()) {
			return new ActionResult<>(EnumActionResult.SUCCESS, otherItem.copy());
		}

		return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(hand));
	}
}
