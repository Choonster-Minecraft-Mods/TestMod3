package choonster.testmod3.client.gui.inventory;

import choonster.testmod3.inventory.container.ContainerModChest;
import choonster.testmod3.inventory.itemhandler.IItemHandlerNameable;
import choonster.testmod3.tileentity.TileEntityModChest;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * GUI for {@link TileEntityModChest}.
 * <p>
 * Adapted from {@link GuiChest}.
 *
 * @author Choonster
 */
public class GuiModChest extends GuiContainer {
	/**
	 * The ResourceLocation containing the chest GUI texture.
	 */
	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("minecraft:textures/gui/container/generic_54.png");

	/**
	 * The colour of the inventory names.
	 */
	private static final int TEXT_COLOUR = 0x404040;

	/**
	 * The player inventory.
	 */
	private final IItemHandlerNameable playerInventory;

	/**
	 * The chest inventory.
	 */
	private final IItemHandlerNameable chestInventory;

	/**
	 * The number of rows in the chest inventory, used to calculate the window height.
	 */
	private final int numRows;

	public GuiModChest(final ContainerModChest container) {
		super(container);
		playerInventory = container.getPlayerInventory();
		chestInventory = container.getChestInventory();

		allowUserInput = false;
		numRows = container.getNumRows();
		ySize = 114 + numRows * 18;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 *
	 * @param mouseX Mouse x coordinate
	 * @param mouseY Mouse y coordinate
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		fontRenderer.drawString(chestInventory.getDisplayName().getString(), 8, 6, TEXT_COLOUR);
		fontRenderer.drawString(playerInventory.getDisplayName().getString(), 8, ySize - 96 + 2, TEXT_COLOUR);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 *
	 * @param partialTicks How far into the current tick the game is, with 0.0 being the start of the tick and 1.0 being the end.
	 * @param mouseX       Mouse x coordinate
	 * @param mouseY       Mouse y coordinate
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);

		mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);

		final int x = (width - xSize) / 2;
		final int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, numRows * 18 + 17);
		drawTexturedModalRect(x, y + numRows * 18 + 17, 0, 126, xSize, 96);
	}
}
