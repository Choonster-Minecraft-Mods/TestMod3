package choonster.testmod3.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
	public void appendHoverText(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
		super.appendHoverText(stack, world, tooltip, flag);

		tooltip.add(new TranslationTextComponent(TestMod3Lang.ITEM_DESC_UNICODE_TOOLTIPS_1.getTranslationKey()));

		tooltip.add(
				new StringTextComponent("§a§o")
						.append(new TranslationTextComponent(TestMod3Lang.ITEM_DESC_UNICODE_TOOLTIPS_2.getTranslationKey()))
						.append("§r")
		);

		tooltip.add(
				new StringTextComponent("" + TextFormatting.GREEN + TextFormatting.ITALIC)
						.append(new TranslationTextComponent(TestMod3Lang.ITEM_DESC_UNICODE_TOOLTIPS_3.getTranslationKey()))
						.append("" + TextFormatting.RESET)
		);
	}
}
