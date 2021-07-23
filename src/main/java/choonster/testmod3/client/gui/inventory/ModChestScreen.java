package choonster.testmod3.client.gui.inventory;

import choonster.testmod3.world.inventory.menu.ModChestMenu;
import choonster.testmod3.world.level.block.entity.ModChestBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI for {@link ModChestBlockEntity}.
 * <p>
 * Adapted from {@link ContainerScreen}.
 *
 * @author Choonster
 */
public class ModChestScreen extends AbstractContainerScreen<ModChestMenu> {
	/**
	 * The ResourceLocation containing the chest GUI texture.
	 */
	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");

	/**
	 * The number of rows in the chest inventory, used to calculate the window height.
	 */
	private final int numRows;

	public ModChestScreen(final ModChestMenu container, final Inventory playerInventory, final Component title) {
		super(container, playerInventory, title);

		passEvents = false;
		numRows = container.getNumRows();
		imageHeight = 114 + numRows * 18;
		inventoryLabelY = imageHeight - 94;
	}

	@Override
	public void render(final PoseStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
		renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(final PoseStack matrixStack, final float partialTicks, final int x, final int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, CHEST_GUI_TEXTURE);

		final int centreX = (width - imageWidth) / 2;
		final int centreY = (height - imageHeight) / 2;
		blit(matrixStack, centreX, centreY, 0, 0, imageWidth, numRows * 18 + 17);
		blit(matrixStack, centreX, centreY + numRows * 18 + 17, 0, 126, imageWidth, 96);
	}
}
