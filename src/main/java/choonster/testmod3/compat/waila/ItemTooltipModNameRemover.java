package choonster.testmod3.compat.waila;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModItems;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Removes the mod name added by Waila to the tooltip of {@link ModItems#NO_MOD_NAME}.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2677402-unable-to-get-current-blocks-tile-entity-metadata?comment=209
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ItemTooltipModNameRemover {
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void itemTooltip(final ItemTooltipEvent event) {
		if (event.getItemStack().getItem() == ModItems.NO_MOD_NAME) {
			event.getToolTip().remove(TextFormatting.BLUE + "" + TextFormatting.ITALIC + TestMod3.NAME);
		}
	}
}
