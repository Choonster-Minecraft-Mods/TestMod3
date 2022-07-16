package choonster.testmod3.event;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModItems;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

/**
 * Combines items in the world.
 * <p>
 * Uses {@link TickEvent.LevelTickEvent} and iterates through {@link ServerLevel#getEntities()} to allow for all input items to be from vanilla or other mods. Creating a dedicated item with a custom entity to act as the controller of this effect would be more efficient.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2728653-better-way-to-check-for-entities-in-world
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID)
public class ItemCombinationHandler {
	/**
	 * The input items.
	 */
	private static final Set<Item> INPUTS = ImmutableSet.of(Items.BONE, Items.BOOK, Items.FEATHER);

	/**
	 * The output item.
	 */
	private static final Supplier<ItemStack> OUTPUT = Lazy.of(() -> new ItemStack(ModItems.GUN.get()));

	private ItemCombinationHandler() {
	}

	@SubscribeEvent
	public static void onWorldTick(final TickEvent.LevelTickEvent event) {
		final var level = event.level;

		// If this is the END phase on the server,
		if (event.phase == TickEvent.Phase.END && !level.isClientSide) {
			// Handle each loaded EntityItem with an input item
			StreamSupport.stream(((ServerLevel) level).getAllEntities().spliterator(), false)
					.filter(isMatchingItemEntity(INPUTS))
					.map(entity -> (ItemEntity) entity)
					.forEach(ItemCombinationHandler::handleEntity);
		}
	}

	/**
	 * Handles the combination effect for an {@link ItemEntity}.
	 *
	 * @param entityItem The item entity
	 */
	private static void handleEntity(final ItemEntity entityItem) {
		// If the item entity is removed, do nothing
		if (!entityItem.isAlive()) {
			return;
		}

		final var level = entityItem.getCommandSenderWorld();

		final Set<Item> remainingInputs = new HashSet<>(INPUTS); // Create a mutable copy of the input set to track which items have been found
		final List<ItemEntity> matchingEntityItems = new ArrayList<>(); // Create a list to track the item entities containing the input items

		remainingInputs.remove(entityItem.getItem().getItem());
		matchingEntityItems.add(entityItem);

		// Find all other item entities with input items within 3 blocks
		final var axisAlignedBB = entityItem.getBoundingBox().inflate(3);
		final var nearbyEntityItems = level.getEntities(entityItem, axisAlignedBB, isMatchingItemEntity(remainingInputs));

		// For each nearby item entity
		nearbyEntityItems.forEach(nearbyEntity -> {
			final var nearbyEntityItem = (ItemEntity) nearbyEntity;
			if (remainingInputs.remove(nearbyEntityItem.getItem().getItem())) { // If the entity's item is a remaining input,
				matchingEntityItems.add(nearbyEntityItem); // Add it to the list of matching item entities

				if (remainingInputs.isEmpty()) { // If all inputs have been found,
					// Spawn the output item at the first item's position
					final double x = entityItem.getX(), y = entityItem.getY(), z = entityItem.getZ();
					final var outputEntityItem = new ItemEntity(level, x, y, z, OUTPUT.get().copy());
					level.addFreshEntity(outputEntityItem);

					((ServerLevel) level).sendParticles(ParticleTypes.LARGE_SMOKE, x + 0.5, y + 1.0, z + 0.5, 1, 0.0, 0.0, 0.0, 0);

					// Consume one item from each matching entity
					matchingEntityItems.forEach(matchingEntityItem -> {
						final var itemStack = matchingEntityItem.getItem();
						itemStack.shrink(1);
						if (itemStack.isEmpty()) {
							matchingEntityItem.discard();
						}
					});
				}
			}
		});

	}

	/**
	 * Returns a predicate that determines whether the entity is a non-removed {@link ItemEntity} whose {@link Item} is contained in the {@link Set}.
	 *
	 * @param items The set of items to match
	 * @return The predicate
	 */
	private static Predicate<Entity> isMatchingItemEntity(final Set<Item> items) {
		return entity -> entity.isAlive() && entity instanceof ItemEntity && items.contains(((ItemEntity) entity).getItem().getItem());
	}
}
