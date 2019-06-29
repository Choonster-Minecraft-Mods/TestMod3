package choonster.testmod3.inventory.container;

import choonster.testmod3.init.ModContainerTypes;
import choonster.testmod3.inventory.IContainerCallbacks;
import choonster.testmod3.inventory.itemhandler.INameableItemHandler;
import choonster.testmod3.inventory.itemhandler.NameableItemHandler;
import choonster.testmod3.inventory.itemhandler.wrapper.NameableCombinedInvWrapper;
import choonster.testmod3.tileentity.ModChestTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

/**
 * A {@link Container} for {@link ModChestTileEntity}.
 * <p>
 * Adapted from {@link ChestContainer}.
 *
 * @author Choonster
 */
public class ModChestContainer extends Container {
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
	private final INameableItemHandler playerInventory;

	/**
	 * The chest inventory.
	 */
	private final INameableItemHandler chestInventory;

	/**
	 * The number of rows in the chest inventory.
	 */
	private final int numRows;

	public ModChestContainer(final int windowID, final PlayerInventory playerInventory) {
		this(windowID, playerInventory, new NameableItemHandler(27, new StringTextComponent("")), IContainerCallbacks.NOOP);
	}

	public ModChestContainer(final int windowID, final PlayerInventory playerInventory, final INameableItemHandler chestInventory, final IContainerCallbacks containerCallbacks) {
		super(ModContainerTypes.CHEST, windowID);
		this.playerInventory = new NameableCombinedInvWrapper(playerInventory, new PlayerMainInvWrapper(playerInventory));
		this.chestInventory = chestInventory;

		callbacks = containerCallbacks;
		callbacks.onContainerOpened(playerInventory.player);

		numRows = chestInventory.getSlots() / SLOTS_PER_ROW;

		final int chestOffset = (numRows - 4) * 18;

		for (int row = 0; row < numRows; ++row) {
			for (int col = 0; col < SLOTS_PER_ROW; ++col) {
				addSlot(new SlotItemHandler(chestInventory, col + row * SLOTS_PER_ROW, 8 + col * 18, 18 + row * 18));
			}
		}

		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < SLOTS_PER_ROW; ++col) {
				addSlot(new SlotItemHandler(this.playerInventory, col + row * SLOTS_PER_ROW + SLOTS_PER_ROW, 8 + col * 18, 103 + row * 18 + chestOffset));
			}
		}

		for (int col = 0; col < SLOTS_PER_ROW; ++col) {
			addSlot(new SlotItemHandler(this.playerInventory, col, 8 + col * 18, 161 + chestOffset));
		}

	}

	@Override
	public ItemStack transferStackInSlot(final PlayerEntity player, final int index) {
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
	public boolean canInteractWith(final PlayerEntity playerIn) {
		return callbacks.isUsableByPlayer(playerIn);
	}

	@Override
	public void onContainerClosed(final PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);

		callbacks.onContainerClosed(playerIn);
	}

	/**
	 * Get the player inventory.
	 *
	 * @return The player inventory
	 */
	public INameableItemHandler getPlayerInventory() {
		return playerInventory;
	}

	/**
	 * Get the chest inventory.
	 *
	 * @return The chest inventory
	 */
	public INameableItemHandler getChestInventory() {
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
