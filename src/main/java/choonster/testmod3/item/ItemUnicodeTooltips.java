package choonster.testmod3.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		super("unicodeTooltips");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);

		tooltip.add(I18n.format("item.testmod3:unicodeTooltips.1.desc"));
		tooltip.add("§a§o" + I18n.format("item.testmod3:unicodeTooltips.2.desc") + "§r");
		tooltip.add("" + TextFormatting.GREEN + TextFormatting.ITALIC + I18n.format("item.testmod3:unicodeTooltips.3.desc") + TextFormatting.RESET);
	}
}
