package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

/**
 * An item that uses unicode characters (specifically the section sign) in its tooltip.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34027.msg179047.html
 *
 * @author Choonster
 */
public class UnicodeTooltipsItem extends Item {
	public UnicodeTooltipsItem(final Item.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(
			final ItemStack stack,
			final TooltipContext context,
			final TooltipDisplay display,
			final Consumer<Component> tooltip,
			final TooltipFlag flag
	) {
		tooltip.accept(Component.translatable(TestMod3Lang.ITEM_DESC_UNICODE_TOOLTIPS_1.getTranslationKey()));

		tooltip.accept(
				Component.literal("§a§o")
						.append(Component.translatable(TestMod3Lang.ITEM_DESC_UNICODE_TOOLTIPS_2.getTranslationKey()))
						.append("§r")
		);

		tooltip.accept(
				Component.literal("" + ChatFormatting.GREEN + ChatFormatting.ITALIC)
						.append(Component.translatable(TestMod3Lang.ITEM_DESC_UNICODE_TOOLTIPS_3.getTranslationKey()))
						.append("" + ChatFormatting.RESET)
		);
	}
}
