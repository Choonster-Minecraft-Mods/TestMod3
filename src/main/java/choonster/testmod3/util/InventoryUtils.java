package choonster.testmod3.util;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;
import net.minecraftforge.items.wrapper.PlayerOffhandInvWrapper;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Utility methods for inventories.
 *
 * @author Choonster
 */
public class InventoryUtils {
	private static final Logger LOGGER = LogUtils.getLogger();

	private static final Field RANDOM_SEQUENCE = ObfuscationReflectionHelper.findField(LootTable.class, /* randomSequence */ "f_286958_");
	private static final Method SHUFFLE_AND_SPLIT_ITEMS = ObfuscationReflectionHelper.findMethod(LootTable.class, /* shuffleAndSplitItems */ "m_79138_", ObjectArrayList.class, int.class, Random.class);

	/**
	 * Fill an {@link IItemHandler} with random loot from a {@link LootTable}.
	 * <p>
	 * Adapted from {@link LootTable#fill}.
	 *
	 * @param itemHandler The inventory to fill with loot
	 * @param lootTable   The LootTable to generate loot from
	 * @param params      The LootParams to use in the loot generation
	 */
	public static void fillItemHandlerWithLoot(final IItemHandler itemHandler, final LootTable lootTable, final LootParams params, final long seed) {
		final Optional<ResourceLocation> randomSequence;

		try {
			@SuppressWarnings("unchecked") final var localRandomSequence = (Optional<ResourceLocation>) RANDOM_SEQUENCE.get(lootTable);
			randomSequence = localRandomSequence;
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get random sequence while generating loot", e);
		}

		final var context = new LootContext.Builder(params)
				.withOptionalRandomSeed(seed)
				.create(randomSequence);

		final var random = context.getRandom();
		final var items = lootTable.getRandomItems(params, seed);
		final var emptySlots = getAvailableSlots(itemHandler, random);

		try {
			SHUFFLE_AND_SPLIT_ITEMS.invoke(lootTable, items, emptySlots.size(), random);
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to shuffle items while generating loot", e);
		}

		for (final var itemStack : items) {
			if (emptySlots.isEmpty()) {
				LOGGER.warn("Tried to over-fill {} while generating loot.", itemHandler);
				return;
			}

			final int slot = emptySlots.remove(emptySlots.size() - 1);
			final var remainder = itemHandler.insertItem(slot, itemStack, false);
			if (!remainder.isEmpty()) {
				LOGGER.warn("Couldn't fully insert {} into slot {} of {}, {} items remain.", itemStack, slot, itemHandler, remainder.getCount());
			}
		}
	}

	/**
	 * Get a list containing the indices of the empty slots in an {@link IItemHandler} in random order.
	 * <p>
	 * Adapted from {@link LootTable}#getAvailableSlots.
	 *
	 * @param itemHandler The inventory
	 * @param random      The Random object
	 * @return The slot indices
	 */
	private static List<Integer> getAvailableSlots(final IItemHandler itemHandler, final RandomSource random) {
		final var emptySlots = new ObjectArrayList<Integer>();

		for (var slot = 0; slot < itemHandler.getSlots(); ++slot) {
			if (itemHandler.getStackInSlot(slot).isEmpty()) {
				emptySlots.add(slot);
			}
		}

		Util.shuffle(emptySlots, random);

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
		for (var slot = 0; slot < itemHandler.getSlots(); slot++) {
			final var stack = itemHandler.extractItem(slot, Integer.MAX_VALUE, false);
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
			return entity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP);
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
		if (entity instanceof final Player player) {
			return LazyOptional.of(() -> new PlayerOffhandInvWrapper(player.getInventory()));
		}

		return entity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP);
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
		if (entity instanceof final Player player) {
			return LazyOptional.of(() -> new PlayerArmorInvWrapper(player.getInventory()));
		}

		return entity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.NORTH);
	}

	/**
	 * Get the entity's inventory of the specified type.
	 *
	 * @param entity        The entity
	 * @param inventoryType The inventory type.
	 * @return A lazy optional containing the inventory, if any
	 */
	public static LazyOptional<IItemHandler> getInventoryForType(final Entity entity, final EntityInventoryType inventoryType) {
		return switch (inventoryType) {
			case MAIN -> getMainInventory(entity);
			case HAND -> getHandInventory(entity);
			case ARMOUR -> getArmourInventory(entity);
		};
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
		for (final var inventoryType : inventoryTypes) {
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
