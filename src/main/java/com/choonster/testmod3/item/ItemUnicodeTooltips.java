package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * An item that uses unicode characters (specifically the section sign) in its tooltip.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34027.msg179047.html
 */
public class ItemUnicodeTooltips extends ItemTestMod3 {
	public ItemUnicodeTooltips() {
		super("unicodeTooltips");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);

		tooltip.add(StatCollector.translateToLocal("item.testmod3:unicodeTooltips.1.desc"));
		tooltip.add("§a§o" + StatCollector.translateToLocal("item.testmod3:unicodeTooltips.2.desc") + "§r");
		tooltip.add("" + EnumChatFormatting.GREEN + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("item.testmod3:unicodeTooltips.3.desc") + EnumChatFormatting.RESET);
	}
}
