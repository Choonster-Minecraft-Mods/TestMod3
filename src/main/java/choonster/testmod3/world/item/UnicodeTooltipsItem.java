package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

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

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(final ItemStack stack, @Nullable final Level world, final List<Component> tooltip, final TooltipFlag flag) {
		super.appendHoverText(stack, world, tooltip, flag);

		tooltip.add(new TranslatableComponent(TestMod3Lang.ITEM_DESC_UNICODE_TOOLTIPS_1.getTranslationKey()));

		tooltip.add(
				new TextComponent("§a§o")
						.append(new TranslatableComponent(TestMod3Lang.ITEM_DESC_UNICODE_TOOLTIPS_2.getTranslationKey()))
						.append("§r")
		);

		tooltip.add(
				new TextComponent("" + ChatFormatting.GREEN + ChatFormatting.ITALIC)
						.append(new TranslatableComponent(TestMod3Lang.ITEM_DESC_UNICODE_TOOLTIPS_3.getTranslationKey()))
						.append("" + ChatFormatting.RESET)
		);
	}
}
