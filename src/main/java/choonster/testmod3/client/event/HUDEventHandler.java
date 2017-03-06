package choonster.testmod3.client.event;

import choonster.testmod3.client.gui.GuiChunkEnergyHUD;
import choonster.testmod3.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Renders this mod's HUDs.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class HUDEventHandler {
	private static final Minecraft minecraft = Minecraft.getMinecraft();
	private static final GuiChunkEnergyHUD chunkEnergyHUD = new GuiChunkEnergyHUD();

	/**
	 * Render the Chunk Energy HUD while the player is holding {@link ModItems#CHUNK_ENERGY_DISPLAY}.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void renderChunkEnergyHUD(RenderGameOverlayEvent.Post event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

		final EntityPlayer player = minecraft.player;
		if (player.getHeldItemMainhand().getItem() != ModItems.CHUNK_ENERGY_DISPLAY && player.getHeldItemOffhand().getItem() != ModItems.CHUNK_ENERGY_DISPLAY)
			return;

		chunkEnergyHUD.drawHUD();
	}
}
