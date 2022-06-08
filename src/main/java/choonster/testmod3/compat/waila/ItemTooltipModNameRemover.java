package choonster.testmod3.compat.waila;

import choonster.testmod3.init.ModItems;

/**
 * Removes the mod name added by Waila to the tooltip of {@link ModItems#NO_MOD_NAME}.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2677402-unable-to-get-current-blocks-tile-entity-metadata?comment=209
 *
 * @author Choonster
 */
/*
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TestMod3.MODID)
public class ItemTooltipModNameRemover {
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void itemTooltip(final ItemTooltipEvent event) {
		if (event.getItemStack().getItem() == ModItems.NO_MOD_NAME.get()) {
			final String name = String.format(Waila.CONFIG.get().getFormatting().getModName(), TestMod3.NAME);
			event.getToolTip().remove(Component.literal(name));
		}
	}
}
*/
