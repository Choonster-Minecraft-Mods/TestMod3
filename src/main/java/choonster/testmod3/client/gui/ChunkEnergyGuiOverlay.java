package choonster.testmod3.client.gui;

import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import choonster.testmod3.config.TestMod3Config;
import choonster.testmod3.init.ModItems;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.CapabilityNotPresentException;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

/**
 * Displays the chunk energy in the player's current chunk.
 *
 * @author Choonster
 */
public final class ChunkEnergyGuiOverlay {
	@SubscribeEvent
	public static void onScreenRenderPost(final ScreenEvent.Render.Post event) {
		final var minecraft = event.getScreen().getMinecraft();

		if (minecraft == null || minecraft.level == null || minecraft.player == null) {
			return;
		}

		final var player = minecraft.player;
		if (!player.getMainHandItem().is(ModItems.CHUNK_ENERGY_DISPLAY.get()) && player.getOffhandItem().is(ModItems.CHUNK_ENERGY_DISPLAY.get())) {
			return;
		}

		final var guiGraphics = event.getGuiGraphics();

		final var chunkEnergy = ChunkEnergyCapability
				.getChunkEnergy(minecraft.level.getChunkAt(minecraft.player.blockPosition()))
				.orElseThrow(CapabilityNotPresentException::new);

		final var text = I18n.get(TestMod3Lang.CHUNK_ENERGY_HUD.getTranslationKey(), chunkEnergy.getEnergyStored(), chunkEnergy.getMaxEnergyStored());
		final var hudPos = TestMod3Config.CLIENT.chunkEnergyHUDPos;
		guiGraphics.drawString(minecraft.font, text, hudPos.x.get(), hudPos.y.get(), 0xFFFFFF);
	}
}
