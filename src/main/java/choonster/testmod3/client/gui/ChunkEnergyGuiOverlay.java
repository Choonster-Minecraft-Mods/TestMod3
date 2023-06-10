package choonster.testmod3.client.gui;

import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import choonster.testmod3.config.TestMod3Config;
import choonster.testmod3.init.ModItems;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.CapabilityNotPresentException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

/**
 * Displays the chunk energy in the player's current chunk.
 *
 * @author Choonster
 */
public class ChunkEnergyGuiOverlay implements IGuiOverlay {
	private final Minecraft minecraft;

	public ChunkEnergyGuiOverlay(final Minecraft minecraft) {
		this.minecraft = minecraft;
	}

	@Override
	public void render(final ForgeGui gui, final GuiGraphics guiGraphics, final float partialTick, final int screenWidth, final int screenHeight) {
		if (minecraft.level == null || minecraft.player == null) {
			return;
		}

		final var player = minecraft.player;
		if (player.getMainHandItem().getItem() != ModItems.CHUNK_ENERGY_DISPLAY.get() && player.getOffhandItem().getItem() != ModItems.CHUNK_ENERGY_DISPLAY.get()) {
			return;
		}

		final var chunkEnergy = ChunkEnergyCapability
				.getChunkEnergy(minecraft.level.getChunkAt(minecraft.player.blockPosition()))
				.orElseThrow(CapabilityNotPresentException::new);

		final var text = I18n.get(TestMod3Lang.CHUNK_ENERGY_HUD.getTranslationKey(), chunkEnergy.getEnergyStored(), chunkEnergy.getMaxEnergyStored());
		final var hudPos = TestMod3Config.CLIENT.chunkEnergyHUDPos;
		guiGraphics.drawString(minecraft.font, text, hudPos.x.get(), hudPos.y.get(), 0xFFFFFF);
	}
}
