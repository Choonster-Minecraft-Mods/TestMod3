package choonster.testmod3.inventory.container;

import choonster.testmod3.inventory.IContainerCallbacks;
import choonster.testmod3.inventory.itemhandler.IItemHandlerNameable;
import choonster.testmod3.tileentity.TileEntityModChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

/**
 * A {@link Container} for {@link TileEntityModChest}.
 * <p>
 * Adapted from {@link ContainerChest}.
 *
 * @author Choonster
 */
public class ContainerModChest extends Container {
	/**
	 * The number of slots per row.
	 */
	private static final int SLOTS_PER_ROW = 9;

	/**
	 * The object to send callbacks to.
	 */
	private final IContainerCallbacks callbacks;

	/**
	 * The player inventory.
	 */
	private final IItemHandlerNameable playerInventory;

	/**
	 * The chest inventory.
	 */
	private final IItemHandlerNameable chestInventory;

	/**
	 * The number of rows in the chest inventory.
	 */
	private final int numRows;

	public ContainerModChest(final IItemHandlerNameable playerInventory, final IItemHandlerNameable chestInventory, final EntityPlayer player, final IContainerCallbacks containerCallbacks) {
		this.playerInventory = playerInventory;
		this.chestInventory = chestInventory;

		callbacks = containerCallbacks;
		callbacks.onContainerOpened(player);

		numRows = chestInventory.getSlots() / SLOTS_PER_ROW;

		final int chestOffset = (numRows - 4) * 18;

		for (int row = 0; row < numRows; ++row) {
			for (int col = 0; col < SLOTS_PER_ROW; ++col) {
				addSlot(new SlotItemHandler(chestInventory, col + row * SLOTS_PER_ROW, 8 + col * 18, 18 + row * 18));
			}
		}

		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < SLOTS_PER_ROW; ++col) {
				addSlot(new SlotItemHandler(playerInventory, col + row * SLOTS_PER_ROW + SLOTS_PER_ROW, 8 + col * 18, 103 + row * 18 + chestOffset));
			}
		}

		for (int col = 0; col < SLOTS_PER_ROW; ++col) {
			addSlot(new SlotItemHandler(playerInventory, col, 8 + col * 18, 161 + chestOffset));
		}

	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer player, final int index) {
		final Slot slot = inventorySlots.get(index);

		if (slot != null && !slot.getStack().isEmpty()) {
			final ItemStack stack = slot.getStack();
			final ItemStack originalStack = stack.copy();

			if (index < numRows * SLOTS_PER_ROW) {
				if (!mergeItemStack(stack, numRows * SLOTS_PER_ROW, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(stack, 0, numRows * SLOTS_PER_ROW, false)) {
				return ItemStack.EMPTY;
			}

			if (stack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			return originalStack;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canInteractWith(final EntityPlayer playerIn) {
		return callbacks.isUsableByPlayer(playerIn);
	}

	@Override
	public void onContainerClosed(final EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);

		callbacks.onContainerClosed(playerIn);
	}

	/**
	 * Get the player inventory.
	 *
	 * @return The player inventory
	 */
	public IItemHandlerNameable getPlayerInventory() {
		return playerInventory;
	}

	/**
	 * Get the chest inventory.
	 *
	 * @return The chest inventory
	 */
	public IItemHandlerNameable getChestInventory() {
		return chestInventory;
	}

	/**
	 * Get the number of rows in the chest inventory.
	 *
	 * @return The number of rows in the chest inventory
	 */
	public int getNumRows() {
		return numRows;
	}
}
