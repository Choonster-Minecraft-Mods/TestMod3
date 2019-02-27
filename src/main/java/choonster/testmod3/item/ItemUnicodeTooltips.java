package choonster.testmod3.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
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
public class ItemUnicodeTooltips extends Item {
	public ItemUnicodeTooltips(final Item.Properties properties) {
		super(properties);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);

		tooltip.add(new TextComponentTranslation("item.testmod3:unicode_tooltips.1.desc"));
		tooltip.add(new TextComponentString("§a§o").appendSibling(new TextComponentTranslation("item.testmod3:unicode_tooltips.2.desc")).appendText("§r"));
		tooltip.add(new TextComponentString("" + TextFormatting.GREEN + TextFormatting.ITALIC).appendSibling(new TextComponentTranslation("item.testmod3:unicode_tooltips.3.desc")).appendText("" + TextFormatting.RESET));
	}
}
