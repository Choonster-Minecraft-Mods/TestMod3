package choonster.testmod3.client.gui;

import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import choonster.testmod3.config.TestMod3Config;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.resources.I18n;

/**
 * Displays the chunk energy in the player's current chunk.
 *
 * @author Choonster
 */
public class GuiChunkEnergyHUD extends AbstractGui {
	private static final Minecraft minecraft = Minecraft.getInstance();

	public void drawHUD(final MatrixStack matrixStack) {
		ChunkEnergyCapability.getChunkEnergy(minecraft.world.getChunkAt(minecraft.player.getPosition()))
				.ifPresent(chunkEnergy -> {
					final String text = I18n.format("testmod3.chunk_energy.hud", chunkEnergy.getEnergyStored(), chunkEnergy.getMaxEnergyStored());
					final TestMod3Config.Client.HUDPos hudPos = TestMod3Config.CLIENT.chunkEnergyHUDPos;
					drawString(matrixStack, minecraft.fontRenderer, text, hudPos.x.get(), hudPos.y.get(), 0xFFFFFF);
				});
	}
}
