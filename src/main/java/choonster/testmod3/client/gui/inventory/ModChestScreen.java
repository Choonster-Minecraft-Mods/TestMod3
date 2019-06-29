package choonster.testmod3.client.gui.inventory;

import choonster.testmod3.inventory.container.ModChestContainer;
import choonster.testmod3.inventory.itemhandler.INameableItemHandler;
import choonster.testmod3.tileentity.ModChestTileEntity;
import com.mojang.blaze3d.platform.GlStateManager;
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
	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("minecraft:textures/gui/container/generic_54.png");

	/**
	 * The colour of the inventory names.
	 */
	private static final int TEXT_COLOUR = 0x404040;

	/**
	 * The player inventory.
	 */
	private final INameableItemHandler playerInventory;

	/**
	 * The chest inventory.
	 */
	private final INameableItemHandler chestInventory;

	/**
	 * The number of rows in the chest inventory, used to calculate the window height.
	 */
	private final int numRows;

	public ModChestScreen(final ModChestContainer container, final PlayerInventory playerInventory, final ITextComponent title) {
		super(container, playerInventory, title);
		this.playerInventory = container.getPlayerInventory();
		chestInventory = container.getChestInventory();

		passEvents = false;
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
		font.drawString(chestInventory.getDisplayName().getString(), 8, 6, TEXT_COLOUR);
		font.drawString(playerInventory.getDisplayName().getString(), 8, ySize - 96 + 2, TEXT_COLOUR);
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

		minecraft.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);

		final int x = (width - xSize) / 2;
		final int y = (height - ySize) / 2;
		blit(x, y, 0, 0, xSize, numRows * 18 + 17);
		blit(x, y + numRows * 18 + 17, 0, 126, xSize, 96);
	}
}
