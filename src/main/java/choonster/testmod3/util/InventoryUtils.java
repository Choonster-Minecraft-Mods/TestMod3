package choonster.testmod3.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;
import net.minecraftforge.items.wrapper.PlayerOffhandInvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

/**
 * Utility methods for inventories.
 *
 * @author Choonster
 */
public class InventoryUtils {
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Get the {@link EquipmentSlot} with the specified index (as returned by {@link EquipmentSlot#getIndex()}.
	 *
	 * @param index The index
	 * @return The equipment slot
	 */
	public static EquipmentSlot getEquipmentSlotFromIndex(final int index) {
		for (final EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
			if (equipmentSlot.getIndex() == index) {
				return equipmentSlot;
			}
		}

		throw new IllegalArgumentException(String.format("Invalid equipment slot index %d", index));
	}

	/**
	 * A reference to {@code LootTable#shuffleAndSplitItems}.
	 */
	private static final Method SHUFFLE_AND_SPLIT_ITEMS = ObfuscationReflectionHelper.findMethod(LootTable.class, /* shuffleAndSplitItems */ "func_186463_a", List.class, int.class, Random.class);

	/**
	 * Fill an {@link IItemHandler} with random loot from a {@link LootTable}.
	 * <p>
	 * Adapted from {@link LootTable#fill}.
	 *
	 * @param itemHandler The inventory to fill with loot
	 * @param lootTable   The LootTable to generate loot from
	 * @param context     The LootContext to use in the loot generation
	 */
	public static void fillItemHandlerWithLoot(final IItemHandler itemHandler, final LootTable lootTable, final LootContext context) {
		final Random random = context.getRandom();
		final List<ItemStack> items = lootTable.getRandomItems(context);
		final List<Integer> emptySlots = getAvailableSlots(itemHandler, random);

		try {
			SHUFFLE_AND_SPLIT_ITEMS.invoke(lootTable, items, emptySlots.size(), random);
		} catch (final Throwable throwable) {
			throw new RuntimeException("Failed to shuffle items while generating loot", throwable);
		}

		for (final ItemStack itemStack : items) {
			if (emptySlots.isEmpty()) {
				LOGGER.warn("Tried to over-fill {} while generating loot.", itemHandler);
				return;
			}

			final int slot = emptySlots.remove(emptySlots.size() - 1);
			final ItemStack remainder = itemHandler.insertItem(slot, itemStack, false);
			if (!remainder.isEmpty()) {
				LOGGER.warn("Couldn't fully insert {} into slot {} of {}, {} items remain.", itemStack, slot, itemHandler, remainder.getCount());
			}
		}
	}

	/**
	 * Get a list containing the indices of the empty slots in an {@link IItemHandler} in random order.
	 * <p>
	 * Adapted from {@link LootTable#getAvailableSlots}.
	 *
	 * @param itemHandler The inventory
	 * @param random      The Random object
	 * @return The slot indices
	 */
	private static List<Integer> getAvailableSlots(final IItemHandler itemHandler, final Random random) {
		final List<Integer> emptySlots = new ArrayList<>();

		for (int slot = 0; slot < itemHandler.getSlots(); ++slot) {
			if (itemHandler.getStackInSlot(slot).isEmpty()) {
				emptySlots.add(slot);
			}
		}

		Collections.shuffle(emptySlots, random);

		return emptySlots;
	}

	/**
	 * Drops the contents of the {@link IItemHandler} in the world.
	 * <p>
	 * Adapted from {@link  Containers#dropContents(Level, BlockPos, Container)}.
	 *
	 * @param level       The level
	 * @param pos         The position to drop the items around
	 * @param itemHandler The inventory to drop the contents of
	 */
	public static void dropItemHandlerContents(final Level level, final BlockPos pos, final IItemHandler itemHandler) {
		for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
			final ItemStack stack = itemHandler.extractItem(slot, Integer.MAX_VALUE, false);
			Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
		}
	}

	/**
	 * An entity inventory type.
	 */
	public enum EntityInventoryType {
		MAIN,
		HAND,
		ARMOUR;

		@Override
		public String toString() {
			return super.toString().toLowerCase(Locale.ENGLISH);
		}
	}

	/**
	 * Get the main inventory of the specified entity.
	 * <p>
	 * For players, this returns the main inventory. For other entities, this returns null.
	 *
	 * @param entity The entity
	 * @return A lazy optional containing the inventory, if any
	 */
	public static LazyOptional<IItemHandler> getMainInventory(final Entity entity) {
		if (entity instanceof Player) {
			return entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
		}

		return LazyOptional.empty();
	}

	/**
	 * Get the hand inventory of the specified entity.
	 * <p>
	 * For players, this returns the off hand inventory. For other entities, this returns the {@link Direction#UP} {@link IItemHandler} capability.
	 *
	 * @param entity The entity
	 * @return A lazy optional containing the hand inventory, if any
	 */
	public static LazyOptional<IItemHandler> getHandInventory(final Entity entity) {
		if (entity instanceof Player) {
			return LazyOptional.of(() -> new PlayerOffhandInvWrapper(((Player) entity).getInventory()));
		}

		return entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
	}

	/**
	 * Get the armour inventory of the specified entity.
	 * <p>
	 * For players, this returns the armour inventory. For other entities, this returns the {@link Direction#NORTH} {@link IItemHandler} capability.
	 *
	 * @param entity The entity
	 * @return A lazy optional containing the inventory, if any
	 */
	public static LazyOptional<IItemHandler> getArmourInventory(final Entity entity) {
		if (entity instanceof Player) {
			return LazyOptional.of(() -> new PlayerArmorInvWrapper(((Player) entity).getInventory()));
		}

		return entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.NORTH);
	}

	/**
	 * Get the entity's inventory of the specified type.
	 *
	 * @param entity        The entity
	 * @param inventoryType The inventory type.
	 * @return A lazy optional containing the inventory, if any
	 */
	public static LazyOptional<IItemHandler> getInventoryForType(final Entity entity, final EntityInventoryType inventoryType) {
		switch (inventoryType) {
			case MAIN:
				return getMainInventory(entity);
			case HAND:
				return getHandInventory(entity);
			case ARMOUR:
				return getArmourInventory(entity);
			default:
				throw new IllegalArgumentException("Unknown inventory type: " + inventoryType);
		}
	}

	/**
	 * Try to perform an operation for each of the specified inventory types, stopping at the first successful operation.
	 * <p>
	 * Only performs the operation on inventory types that exist for the entity.
	 * <p>
	 * This is mainly useful in {@link Item#inventoryTick(ItemStack, Level, Entity, int, boolean)}, where the item can be in any of the player's inventories.
	 *
	 * @param entity         The entity
	 * @param operation      The operation to perform
	 * @param inventoryTypes The inventory types to perform the operation on, in order
	 * @return The inventory type of the first successful operation, or null if all operations failed
	 */
	public static Optional<EntityInventoryType> forEachEntityInventory(final Entity entity, final Predicate<IItemHandler> operation, final EntityInventoryType... inventoryTypes) {
		for (final EntityInventoryType inventoryType : inventoryTypes) {
			final boolean result = getInventoryForType(entity, inventoryType)
					.map(operation::test)
					.orElse(false);

			if (result) {
				return Optional.of(inventoryType);
			}
		}

		return Optional.empty();
	}
}
