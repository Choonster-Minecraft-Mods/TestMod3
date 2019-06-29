package choonster.testmod3.client.gui;

import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import choonster.testmod3.config.TestMod3Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;

/**
 * Displays the chunk energy in the player's current chunk.
 *
 * @author Choonster
 */
public class GuiChunkEnergyHUD extends AbstractGui {
	private static final Minecraft minecraft = Minecraft.getInstance();

	public void drawHUD() {
		ChunkEnergyCapability.getChunkEnergy(minecraft.world.getChunkAt(new BlockPos(minecraft.player)))
				.ifPresent(chunkEnergy -> {
					final String text = I18n.format("testmod3.chunk_energy.hud", chunkEnergy.getEnergyStored(), chunkEnergy.getMaxEnergyStored());
					final TestMod3Config.Client.HUDPos hudPos = TestMod3Config.CLIENT.chunkEnergyHUDPos;
					drawString(minecraft.fontRenderer, text, hudPos.x.get(), hudPos.y.get(), 0xFFFFFF);
				});
	}
}
