package choonster.testmod3.world.level.block.entity;

import choonster.testmod3.capability.lock.Lock;
import choonster.testmod3.init.ModBlockEntities;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.world.inventory.IMenuCallbacks;
import choonster.testmod3.world.inventory.itemhandler.BlockEntityLootItemHandler;
import choonster.testmod3.world.inventory.itemhandler.LootItemHandler;
import choonster.testmod3.world.inventory.menu.ModChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;

import org.jetbrains.annotations.Nullable;

/**
 * A chest that uses {@link IItemHandler} instead of {@link Container}.
 * <p>
 * Adapted from {@link ChestBlockEntity}.
 *
 * @author Choonster
 */
public class ModChestBlockEntity extends LootItemHandlerBlockEntity implements IMenuCallbacks {
	/**
	 * The number of slots in this chest's inventory.
	 */
	private static final int INVENTORY_SIZE = 27;

	/**
	 * The default name of this chest's inventory.
	 */
	private static final Component DEFAULT_NAME = Component.translatable(TestMod3Lang.CONTAINER_CHEST.getTranslationKey());

	public ModChestBlockEntity(final BlockPos pos, final BlockState state) {
		super(ModBlockEntities.MOD_CHEST.get(), pos, state);
	}

	/**
	 * Create and return the inventory.
	 *
	 * @return The inventory
	 */
	@Override
	protected LootItemHandler createInventory() {
		return new BlockEntityLootItemHandler(INVENTORY_SIZE, this);
	}

	@Override
	protected Component getDefaultName() {
		return DEFAULT_NAME;
	}

	/**
	 * Create and return the lock.
	 *
	 * @return The lock
	 */
	@Override
	protected Lock createLock() {
		return new Lock(getNameHolder());
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(final int windowID, final Inventory playerInventory, final Player player) {
		inventory.fillWithLoot(player);

		return new ModChestMenu(windowID, playerInventory, this);
	}

	/**
	 * Called when the {@link Container} is opened by a player.
	 *
	 * @param player The player
	 */
	@Override
	public void onMenuOpened(final Player player) {

	}

	/**
	 * Called when the {@link Container} is closed by a player.
	 *
	 * @param player The player
	 */
	@Override
	public void onMenuClosed(final Player player) {

	}

	/**
	 * Is this usable by the specified player?
	 *
	 * @param player The player
	 * @return Is this usable by the specified player?
	 */
	@Override
	public boolean isUsableByPlayer(final Player player) {
		return level.getBlockEntity(worldPosition) == this && player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64;
	}
}
