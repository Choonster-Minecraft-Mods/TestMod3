package choonster.testmod3.client.event;

import choonster.testmod3.TestMod3;
import choonster.testmod3.client.gui.GuiChunkEnergyHUD;
import choonster.testmod3.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Renders this mod's HUDs.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TestMod3.MODID)
public class HUDEventHandler {
	private static final Minecraft minecraft = Minecraft.getInstance();
	private static final GuiChunkEnergyHUD chunkEnergyHUD = new GuiChunkEnergyHUD();

	/**
	 * Render the Chunk Energy HUD while the player is holding {@link ModItems#CHUNK_ENERGY_DISPLAY}.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void renderChunkEnergyHUD(final RenderGameOverlayEvent.Post event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
			return;
		}

		if (minecraft.player == null) {
			return;
		}

		final Player player = minecraft.player;
		if (player.getMainHandItem().getItem() != ModItems.CHUNK_ENERGY_DISPLAY.get() && player.getOffhandItem().getItem() != ModItems.CHUNK_ENERGY_DISPLAY.get()) {
			return;
		}

		chunkEnergyHUD.drawHUD(event.getPoseStack());
	}
}
