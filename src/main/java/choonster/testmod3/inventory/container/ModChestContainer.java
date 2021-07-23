package choonster.testmod3.inventory.container;

import choonster.testmod3.init.ModContainerTypes;
import choonster.testmod3.inventory.IContainerCallbacks;
import choonster.testmod3.tileentity.ModChestTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.items.IItemHandler;
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
	 * The number of rows in the chest inventory.
	 */
	private final int numRows;

	public ModChestContainer(final int windowID, final PlayerInventory playerInventory, final ModChestTileEntity tileEntity) {
		super(ModContainerTypes.CHEST.get(), windowID);

		callbacks = tileEntity;
		callbacks.onContainerOpened(playerInventory.player);

		final IItemHandler playerInventoryItemHandler = new PlayerMainInvWrapper(playerInventory);
		final IItemHandler chestInventory = tileEntity.getInventory();

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
	public ItemStack quickMoveStack(final PlayerEntity player, final int index) {
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
	public boolean stillValid(final PlayerEntity playerIn) {
		return callbacks.isUsableByPlayer(playerIn);
	}

	@Override
	public void removed(final PlayerEntity playerIn) {
		super.removed(playerIn);

		callbacks.onContainerClosed(playerIn);
	}

	/**
	 * Get the number of rows in the chest inventory.
	 *
	 * @return The number of rows in the chest inventory
	 */
	public int getNumRows() {
		return numRows;
	}

	public static class Factory implements IContainerFactory<ModChestContainer> {
		@Override
		public ModChestContainer create(final int windowId, final PlayerInventory inv, final PacketBuffer data) {
			final BlockPos pos = data.readBlockPos();
			final World world = inv.player.getCommandSenderWorld();
			final TileEntity tileEntity = world.getBlockEntity(pos);

			if (!(tileEntity instanceof ModChestTileEntity)) {
				throw new IllegalStateException("Invalid block at " + pos);
			}

			return new ModChestContainer(windowId, inv, (ModChestTileEntity) tileEntity);
		}
	}
}
