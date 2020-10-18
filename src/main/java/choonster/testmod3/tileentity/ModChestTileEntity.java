package choonster.testmod3.tileentity;

import choonster.testmod3.capability.lock.Lock;
import choonster.testmod3.init.ModTileEntities;
import choonster.testmod3.inventory.IContainerCallbacks;
import choonster.testmod3.inventory.container.ModChestContainer;
import choonster.testmod3.inventory.itemhandler.LootItemHandler;
import choonster.testmod3.inventory.itemhandler.TileEntityLootItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * A chest that uses {@link IItemHandler} instead of {@link IInventory}.
 * <p>
 * Adapted from {@link ChestTileEntity}.
 *
 * @author Choonster
 */
public class ModChestTileEntity extends LootItemHandlerTileEntity implements IContainerCallbacks {
	/**
	 * The number of slots in this chest's inventory.
	 */
	private static final int INVENTORY_SIZE = 27;

	/**
	 * The default name of this chest's inventory.
	 */
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.testmod3.chest");

	public ModChestTileEntity() {
		super(ModTileEntities.MOD_CHEST.get());
	}

	/**
	 * Create and return the inventory.
	 *
	 * @return The inventory
	 */
	@Override
	protected LootItemHandler createInventory() {
		return new TileEntityLootItemHandler(INVENTORY_SIZE, DEFAULT_NAME, this);
	}

	/**
	 * Create and return the lock.
	 *
	 * @return The lock
	 */
	@Override
	protected Lock createLock() {
		return new Lock(DEFAULT_NAME);
	}

	@Nullable
	@Override
	public Container createMenu(final int windowID, final PlayerInventory playerInventory, final PlayerEntity player) {
		inventory.fillWithLoot(player);

		return new ModChestContainer(windowID, playerInventory, inventory, this);
	}

	/**
	 * Called when the {@link Container} is opened by a player.
	 *
	 * @param player The player
	 */
	@Override
	public void onContainerOpened(final PlayerEntity player) {

	}

	/**
	 * Called when the {@link Container} is closed by a player.
	 *
	 * @param player The player
	 */
	@Override
	public void onContainerClosed(final PlayerEntity player) {

	}

	/**
	 * Is this usable by the specified player?
	 *
	 * @param player The player
	 * @return Is this usable by the specified player?
	 */
	@Override
	public boolean isUsableByPlayer(final PlayerEntity player) {
		return world.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
	}

	/**
	 * Set the display name of this chest's inventory.
	 *
	 * @param displayName The display name
	 */
	public void setDisplayName(final ITextComponent displayName) {
		inventory.setCustomName(displayName);
		lock.setDisplayName(displayName);
	}
}
