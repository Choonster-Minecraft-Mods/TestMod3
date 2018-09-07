package choonster.testmod3.client.gui;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.chunkenergy.CapabilityChunkEnergy;
import choonster.testmod3.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;

/**
 * Displays the chunk energy in the player's current chunk.
 *
 * @author Choonster
 */
public class GuiChunkEnergyHUD extends Gui {
	private static final Minecraft minecraft = Minecraft.getMinecraft();

	public void drawHUD() {
		final IChunkEnergy chunkEnergy = CapabilityChunkEnergy.getChunkEnergy(minecraft.world.getChunk(new BlockPos(minecraft.player)));
		if (chunkEnergy == null) return;

		final String text = I18n.format("testmod3:chunk_energy.hud", chunkEnergy.getEnergyStored(), chunkEnergy.getMaxEnergyStored());
		final ModConfig.Client.HUDPos hudPos = ModConfig.client.chunkEnergyHUDPos;
		drawString(minecraft.fontRenderer, text, hudPos.x, hudPos.y, 0xFFFFFF);
	}
}
