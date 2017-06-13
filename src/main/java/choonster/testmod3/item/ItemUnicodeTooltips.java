package choonster.testmod3.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
public class ItemUnicodeTooltips extends ItemTestMod3 {
	public ItemUnicodeTooltips() {
		super("unicode_tooltips");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, @Nullable final World world, final List<String> tooltip, final ITooltipFlag advanced) {
		super.addInformation(stack, world, tooltip, advanced);

		tooltip.add(I18n.format("item.testmod3:unicode_tooltips.1.desc"));
		tooltip.add("§a§o" + I18n.format("item.testmod3:unicode_tooltips.2.desc") + "§r");
		tooltip.add("" + TextFormatting.GREEN + TextFormatting.ITALIC + I18n.format("item.testmod3:unicode_tooltips.3.desc") + TextFormatting.RESET);
	}
}
