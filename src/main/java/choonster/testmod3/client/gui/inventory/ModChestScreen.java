package choonster.testmod3.client.gui.inventory;

import choonster.testmod3.world.inventory.menu.ModChestMenu;
import choonster.testmod3.world.level.block.entity.ModChestBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
	 * The Identifier containing the chest GUI texture.
	 */
	private static final Identifier CONTAINER_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");

	/**
	 * The number of rows in the chest inventory, used to calculate the window height.
	 */
	private final int containerRows;

	public ModChestScreen(final ModChestMenu menu, final Inventory playerInventory, final Component title) {
		super(menu, playerInventory, title, 176, 114 + menu.getRowCount() * 18);
		containerRows = this.menu.getRowCount();
		inventoryLabelY = imageHeight - 94;
	}

	@Override
	public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);

		final var centreX = (width - imageWidth) / 2;
		final var centreY = (height - imageHeight) / 2;

		graphics.blit(
				RenderPipelines.GUI_TEXTURED,
				CONTAINER_BACKGROUND,
				centreX,
				centreY,
				0,
				0,
				imageWidth,
				containerRows * 18 + 17,
				256,
				256
		);

		graphics.blit(
				RenderPipelines.GUI_TEXTURED,
				CONTAINER_BACKGROUND,
				centreX,
				centreY + containerRows * 18 + 17,
				0,
				126,
				imageWidth,
				96,
				256,
				256
		);
	}
}
