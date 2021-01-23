package choonster.testmod3.client.gui.inventory;

import choonster.testmod3.inventory.container.ModChestContainer;
import choonster.testmod3.tileentity.ModChestTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * GUI for {@link ModChestTileEntity}.
 * <p>
 * Adapted from {@link ChestScreen}.
 *
 * @author Choonster
 */
public class ModChestScreen extends ContainerScreen<ModChestContainer> {
	/**
	 * The ResourceLocation containing the chest GUI texture.
	 */
	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");

	/**
	 * The number of rows in the chest inventory, used to calculate the window height.
	 */
	private final int numRows;

	public ModChestScreen(final ModChestContainer container, final PlayerInventory playerInventory, final ITextComponent title) {
		super(container, playerInventory, title);

		passEvents = false;
		numRows = container.getNumRows();
		ySize = 114 + numRows * 18;
		playerInventoryTitleY = ySize - 94;
	}

	@Override
	public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
		renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final MatrixStack matrixStack, final float partialTicks, final int x, final int y) {
		// TODO: Figure out how to render texture with colour without using this method
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);

		minecraft.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);

		final int centreX = (width - xSize) / 2;
		final int centreY = (height - ySize) / 2;
		blit(matrixStack, centreX, centreY, 0, 0, xSize, numRows * 18 + 17);
		blit(matrixStack, centreX, centreY + numRows * 18 + 17, 0, 126, xSize, 96);
	}
}
