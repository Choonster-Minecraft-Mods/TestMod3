package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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
	public void appendHoverText(final ItemStack stack, @Nullable final Level world, final List<Component> tooltip, final TooltipFlag flag) {
		final ItemStack otherItem = getOtherItem();

		if (!otherItem.isEmpty()) {
			tooltip.add(Component.translatable(TestMod3Lang.ITEM_DESC_SWAP_TEST_WITH_ITEM.getTranslationKey(), otherItem.getHoverName()));
		} else {
			tooltip.add(Component.translatable(TestMod3Lang.ITEM_DESC_SWAP_TEST_WITHOUT_ITEM.getTranslationKey()));
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level level, final Player playerIn, final InteractionHand hand) {
		final ItemStack otherItem = getOtherItem();

		if (!otherItem.isEmpty() && playerIn.isShiftKeyDown()) {
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, otherItem.copy());
		}

		return new InteractionResultHolder<>(InteractionResult.PASS, playerIn.getItemInHand(hand));
	}
}
