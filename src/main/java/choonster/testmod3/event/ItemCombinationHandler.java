package choonster.testmod3.event;

import choonster.testmod3.init.ModItems;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Combines items in the world.
 * <p>
 * Uses {@link TickEvent.WorldTickEvent} and iterates through {@link World#loadedEntityList} to allow for all input items to be from vanilla or other mods. Creating a dedicated item with a custom entity to act as the controller of this effect would be more efficient.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2728653-better-way-to-check-for-entities-in-world
 *
 * @author Choonster
 */
public class ItemCombinationHandler {
	/**
	 * The input items.
	 */
	private static final Set<Item> INPUTS = ImmutableSet.of(Items.BONE, Items.BOOK, Items.FEATHER);

	/**
	 * The output item.
	 */
	private static final ItemStack OUTPUT = new ItemStack(ModItems.GUN);

	private ItemCombinationHandler() {
	}

	@SubscribeEvent
	public static void onWorldTick(final TickEvent.WorldTickEvent event) {
		final World world = event.world;

		// If this is the END phase on the server,
		if (event.phase == TickEvent.Phase.END && !world.isRemote) {

			// Handle each loaded EntityItem with an input item
			world.loadedEntityList.stream()
					.filter(isMatchingItemEntity(INPUTS))
					.map(entity -> (EntityItem) entity)
					.collect(Collectors.toList())
					.forEach(ItemCombinationHandler::handleEntity);
		}
	}

	/**
	 * Handles the combination effect for an {@link EntityItem}.
	 *
	 * @param entityItem The item entity
	 */
	private static void handleEntity(final EntityItem entityItem) {
		// If the item entity is dead, do nothing
		if (!entityItem.isEntityAlive()) return;

		final World world = entityItem.getEntityWorld();

		final Set<Item> remainingInputs = new HashSet<>(INPUTS); // Create a mutable copy of the input set to track which items have been found
		final List<EntityItem> matchingEntityItems = new ArrayList<>(); // Create a list to track the item entities containing the input items

		remainingInputs.remove(entityItem.getEntityItem().getItem());
		matchingEntityItems.add(entityItem);

		// Find all other item entities with input items within 3 blocks
		final AxisAlignedBB axisAlignedBB = entityItem.getEntityBoundingBox().expandXyz(3);
		final List<Entity> nearbyEntityItems = world.getEntitiesInAABBexcluding(entityItem, axisAlignedBB, isMatchingItemEntity(remainingInputs)::test);

		// For each nearby item entity
		nearbyEntityItems.forEach(nearbyEntity -> {
			final EntityItem nearbyEntityItem = (EntityItem) nearbyEntity;
			if (remainingInputs.remove(nearbyEntityItem.getEntityItem().getItem())) { // If the entity's item is a remaining input,
				matchingEntityItems.add(nearbyEntityItem); // Add it to the list of matching item entities

				if (remainingInputs.isEmpty()) { // If all inputs have been found,
					// Spawn the output item at the first item's position
					final double x = entityItem.posX, y = entityItem.posY, z = entityItem.posZ;
					final EntityItem outputEntityItem = new EntityItem(world, x, y, z, OUTPUT.copy());
					world.spawnEntityInWorld(outputEntityItem);

					((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + 0.5, y + 1.0, z + 0.5, 1, 0, 0, 0, 0, new int[0]);

					// Consume one item from each matching entity
					matchingEntityItems.forEach(matchingEntityItem -> {
						final ItemStack itemStack = matchingEntityItem.getEntityItem();
						itemStack.stackSize--;
						if (itemStack.stackSize <= 0) {
							matchingEntityItem.setDead();
						}
					});
				}
			}
		});

	}

	/**
	 * Returns a predicate that determines whether the entity is a living {@link EntityItem} whose {@link Item} is contained in the {@link Set}.
	 *
	 * @param items The set of items to match
	 * @return The predicate
	 */
	private static Predicate<Entity> isMatchingItemEntity(Set<Item> items) {
		return entity -> entity.isEntityAlive() && entity instanceof EntityItem && items.contains(((EntityItem) entity).getEntityItem().getItem());
	}
}
