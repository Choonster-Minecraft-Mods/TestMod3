package choonster.testmod3.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * An item that's converted to another when shift-right clicked.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34244.0.html
 *
 * @author Choonster
 */
public class SwapTestItem extends Item {
	private final Supplier<ItemStack> otherItem;

	public SwapTestItem(final Item.Properties properties, final Supplier<ItemStack> otherItem) {
		super(properties);
		this.otherItem = Lazy.of(otherItem);
	}

	private ItemStack getOtherItem() {
		return otherItem.get();
	}

	@Override
	public void addInformation(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
		final ItemStack otherItem = getOtherItem();

		if (!otherItem.isEmpty()) {
			tooltip.add(new TranslationTextComponent(TestMod3Lang.ITEM_DESC_SWAP_TEST_WITH_ITEM.getTranslationKey(), otherItem.getDisplayName()));
		} else {
			tooltip.add(new TranslationTextComponent(TestMod3Lang.ITEM_DESC_SWAP_TEST_WITHOUT_ITEM.getTranslationKey()));
		}
	}


	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final PlayerEntity playerIn, final Hand hand) {
		final ItemStack otherItem = getOtherItem();

		if (!otherItem.isEmpty() && playerIn.isSneaking()) {
			return new ActionResult<>(ActionResultType.SUCCESS, otherItem.copy());
		}

		return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(hand));
	}
}
