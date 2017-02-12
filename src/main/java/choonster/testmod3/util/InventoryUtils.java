package choonster.testmod3.util;

import choonster.testmod3.Logger;
import com.google.common.base.Throwables;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;
import net.minecraftforge.items.wrapper.PlayerOffhandInvWrapper;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.util.*;
import java.util.function.Predicate;

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
			SHUFFLE_ITEMS.invokeExact(lootTable, items, emptySlots.size(), random);
		} catch (Throwable throwable) {
			Throwables.propagate(throwable);
		}

		for (final ItemStack itemStack : items) {
			if (emptySlots.isEmpty()) {
				Logger.warn("Tried to over-fill %s while generating loot.");
				return;
			}

			final int slot = emptySlots.remove(emptySlots.size() - 1);
			final ItemStack remainder = itemHandler.insertItem(slot, itemStack, false);
			if (!remainder.isEmpty()) {
				Logger.warn("Couldn't fully insert %s into slot %d of %s, %d items remain.", itemStack, slot, itemHandler, remainder.getCount());
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
			if (itemHandler.getStackInSlot(slot).isEmpty()) {
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
			while (!itemHandler.getStackInSlot(slot).isEmpty()) {
				final int amount = random.nextInt(21) + 10;

				if (!itemHandler.extractItem(slot, amount, true).isEmpty()) {
					final ItemStack itemStack = itemHandler.extractItem(slot, amount, false);
					drops.add(itemStack);
				}
			}
		}

		return drops;
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
	 * @return The inventory, if any
	 */
	@Nullable
	public static IItemHandler getMainInventory(Entity entity) {
		if (entity instanceof EntityPlayer) {
			return entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		}

		return null;
	}

	/**
	 * Get the hand inventory of the specified entity.
	 * <p>
	 * For players, this returns the off hand inventory. For other entities, this returns the {@link EnumFacing#UP} {@link IItemHandler} capability.
	 *
	 * @param entity The entity
	 * @return The hand inventory, if any
	 */
	@Nullable
	public static IItemHandler getHandInventory(Entity entity) {
		if (entity instanceof EntityPlayer) {
			return new PlayerOffhandInvWrapper(((EntityPlayer) entity).inventory);
		}

		return entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
	}

	/**
	 * Get the armour inventory of the specified entity.
	 * <p>
	 * For players, this returns the armour inventory. For other entities, this returns the {@link EnumFacing#NORTH} {@link IItemHandler} capability.
	 *
	 * @param entity The entity
	 * @return The inventory, if any
	 */
	@Nullable
	public static IItemHandler getArmourInventory(Entity entity) {
		if (entity instanceof EntityPlayer) {
			return new PlayerArmorInvWrapper(((EntityPlayer) entity).inventory);
		}

		return entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
	}

	/**
	 * Get the entity's inventory of the specified type.
	 *
	 * @param entity        The entity
	 * @param inventoryType The inventory type.
	 * @return The inventory, if any
	 */
	@Nullable
	public static IItemHandler getInventoryForType(final Entity entity, final EntityInventoryType inventoryType) {
		switch (inventoryType) {
			case MAIN:
				return getMainInventory(entity);
			case HAND:
				return getHandInventory(entity);
			case ARMOUR:
				return getArmourInventory(entity);
		}

		return null;
	}

	/**
	 * Try to perform an operation for each of the specified inventory types, stopping at the first successful operation.
	 * <p>
	 * Only performs the operation on inventory types that exist for the entity.
	 * <p>
	 * This is mainly useful in {@link Item#onUpdate(ItemStack, World, Entity, int, boolean)}, where the item can be in any of the player's inventories.
	 *
	 * @param entity         The entity
	 * @param operation      The operation to perform
	 * @param inventoryTypes The inventory types to perform the operation on, in order
	 * @return The inventory type of the first successful operation, or null if all operations failed
	 */
	@Nullable
	public static EntityInventoryType forEachEntityInventory(final Entity entity, final Predicate<IItemHandler> operation, final EntityInventoryType... inventoryTypes) {
		for (final EntityInventoryType inventoryType : inventoryTypes) {
			final IItemHandler inventory = getInventoryForType(entity, inventoryType);
			if (inventory != null && operation.test(inventory)) {
				return inventoryType;
			}
		}

		return null;
	}
}
