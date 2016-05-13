package com.choonster.testmod3.util;

import com.choonster.testmod3.Logger;
import com.google.common.base.Throwables;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.items.IItemHandler;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Utility methods for inventories.
 *
 * @author Choonster
 */
public class InventoryUtils {

	/**
	 * Get the {@link EntityEquipmentSlot} with the specified index (as returned by {@link EntityEquipmentSlot#getSlotIndex()}.
	 *
	 * @param index The index
	 * @return The equipment slot
	 */
	public static EntityEquipmentSlot getEquipmentSlotFromIndex(int index) {
		for (final EntityEquipmentSlot equipmentSlot : EntityEquipmentSlot.values()) {
			if (equipmentSlot.getSlotIndex() == index) {
				return equipmentSlot;
			}
		}

		throw new IllegalArgumentException(String.format("Invalid equipment slot index %d", index));
	}

	/**
	 * A reference to {@link LootTable#shuffleItems}.
	 */
	private static final MethodHandle SHUFFLE_ITEMS = ReflectionUtil.findMethod(LootTable.class, new String[]{"shuffleItems", "func_186463_a"}, List.class, int.class, Random.class);

	/**
	 * Fill an {@link IItemHandler} with random loot from a {@link LootTable}.
	 * <p>
	 * Adapted from {@link LootTable#fillInventory}.
	 *
	 * @param itemHandler The inventory to fill with loot
	 * @param lootTable   The LootTable to generate loot from
	 * @param random      The Random object to use in the loot generation
	 * @param context     The LootContext to use in the loot generation
	 */
	public static void fillItemHandlerWithLoot(IItemHandler itemHandler, LootTable lootTable, Random random, LootContext context) {
		final List<ItemStack> items = lootTable.generateLootForPools(random, context);
		final List<Integer> emptySlots = getEmptySlotsRandomized(itemHandler, random);

		try {
			SHUFFLE_ITEMS.invoke(lootTable, items, emptySlots.size(), random);
		} catch (Throwable throwable) {
			Throwables.propagate(throwable);
		}

		for (ItemStack itemStack : items) {
			if (emptySlots.isEmpty()) {
				Logger.warn("Tried to over-fill %s while generating loot.");
				return;
			}

			final int slot = emptySlots.remove(emptySlots.size() - 1);
			final ItemStack remainder = itemHandler.insertItem(slot, itemStack, false);
			if (remainder != null) {
				Logger.warn("Couldn't fully insert %s into slot %d of %s, %d items remain.", itemStack, slot, itemHandler, remainder.stackSize);
			}
		}
	}

	/**
	 * Get a list containing the indices of the empty slots in an {@link IItemHandler} in random order.
	 * <p>
	 * Adapted from {@link LootTable#getEmptySlotsRandomized}.
	 *
	 * @param itemHandler The inventory
	 * @param random      The Random object
	 * @return The slot indices
	 */
	private static List<Integer> getEmptySlotsRandomized(IItemHandler itemHandler, Random random) {
		final List<Integer> emptySlots = new ArrayList<>();

		for (int slot = 0; slot < itemHandler.getSlots(); ++slot) {
			if (itemHandler.getStackInSlot(slot) == null) {
				emptySlots.add(slot);
			}
		}

		Collections.shuffle(emptySlots, random);

		return emptySlots;
	}

	/**
	 * Get a list of the {@link IItemHandler}'s contents with the stacks randomly split.
	 * <p>
	 * Adapted from {@link InventoryHelper#dropInventoryItems}.
	 *
	 * @param itemHandler The inventory
	 * @param random      The Random object
	 * @return The drops list
	 */
	public static List<ItemStack> dropItemHandlerContents(IItemHandler itemHandler, Random random) {
		final List<ItemStack> drops = new ArrayList<>();

		for (int slot = 0; slot < itemHandler.getSlots(); ++slot) {
			while (itemHandler.getStackInSlot(slot) != null) {
				final int amount = random.nextInt(21) + 10;

				if (itemHandler.extractItem(slot, amount, true) != null) {
					final ItemStack itemStack = itemHandler.extractItem(slot, amount, false);
					drops.add(itemStack);
				}
			}
		}

		return drops;
	}
}
