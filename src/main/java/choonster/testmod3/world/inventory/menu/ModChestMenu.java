package choonster.testmod3.world.inventory.menu;

import choonster.testmod3.init.ModMenuTypes;
import choonster.testmod3.world.inventory.IMenuCallbacks;
import choonster.testmod3.world.level.block.entity.ModChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.network.IContainerFactory;

/**
 * A {@link AbstractContainerMenu} for {@link ModChestBlockEntity}.
 * <p>
 * Adapted from {@link ChestMenu}.
 *
 * @author Choonster
 */
public class ModChestMenu extends AbstractContainerMenu {
	/**
	 * The number of slots per row.
	 */
	private static final int SLOTS_PER_ROW = 9;

	/**
	 * The object to send callbacks to.
	 */
	private final IMenuCallbacks callbacks;

	/**
	 * The number of rows in the chest inventory.
	 */
	private final int numRows;

	public ModChestMenu(final int windowID, final Inventory playerInventory, final ModChestBlockEntity blockEntity) {
		super(ModMenuTypes.CHEST.get(), windowID);

		callbacks = blockEntity;
		callbacks.onMenuOpened(playerInventory.player);

		final IItemHandler playerInventoryItemHandler = new PlayerMainInvWrapper(playerInventory);
		final IItemHandler chestInventory = blockEntity.getInventory();

		numRows = chestInventory.getSlots() / SLOTS_PER_ROW;

		final int chestOffset = (numRows - 4) * 18;

		for (int row = 0; row < numRows; ++row) {
			for (int col = 0; col < SLOTS_PER_ROW; ++col) {
				addSlot(new SlotItemHandler(chestInventory, col + row * SLOTS_PER_ROW, 8 + col * 18, 18 + row * 18));
			}
		}

		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < SLOTS_PER_ROW; ++col) {
				addSlot(new SlotItemHandler(playerInventoryItemHandler, col + row * SLOTS_PER_ROW + SLOTS_PER_ROW, 8 + col * 18, 103 + row * 18 + chestOffset));
			}
		}

		for (int col = 0; col < SLOTS_PER_ROW; ++col) {
			addSlot(new SlotItemHandler(playerInventoryItemHandler, col, 8 + col * 18, 161 + chestOffset));
		}
	}

	@Override
	public ItemStack quickMoveStack(final Player player, final int index) {
		final Slot slot = slots.get(index);

		if (slot != null && slot.hasItem()) {
			final ItemStack stack = slot.getItem();
			final ItemStack originalStack = stack.copy();

			if (index < numRows * SLOTS_PER_ROW) {
				if (!moveItemStackTo(stack, numRows * SLOTS_PER_ROW, slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!moveItemStackTo(stack, 0, numRows * SLOTS_PER_ROW, false)) {
				return ItemStack.EMPTY;
			}

			if (stack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			return originalStack;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(final Player playerIn) {
		return callbacks.isUsableByPlayer(playerIn);
	}

	@Override
	public void removed(final Player playerIn) {
		super.removed(playerIn);

		callbacks.onMenuClosed(playerIn);
	}

	/**
	 * Get the number of rows in the chest inventory.
	 *
	 * @return The number of rows in the chest inventory
	 */
	public int getNumRows() {
		return numRows;
	}

	public static class Factory implements IContainerFactory<ModChestMenu> {
		@Override
		public ModChestMenu create(final int windowId, final Inventory inv, final FriendlyByteBuf data) {
			final BlockPos pos = data.readBlockPos();
			final Level world = inv.player.getCommandSenderWorld();
			final BlockEntity blockEntity = world.getBlockEntity(pos);

			if (!(blockEntity instanceof ModChestBlockEntity)) {
				throw new IllegalStateException("Invalid block at " + pos);
			}

			return new ModChestMenu(windowId, inv, (ModChestBlockEntity) blockEntity);
		}
	}
}
