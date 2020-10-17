package choonster.testmod3.tweak.spawnerdrops;

import choonster.testmod3.TestMod3;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Allows Mob Spawners to be dropped when broken with a Silk Touch pickaxe.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2677402-unable-to-get-current-blocks-tile-entity-metadata
 *
 * @author Choonster
 */
// TODO: Convert to Global Loot Modifier
@Mod.EventBusSubscriber(modid = TestMod3.MODID)
public class SpawnerDrops {

	/**
	 * Stores the TileEntities of broken spawner blocks between {@link BlockEvent.BreakEvent} and BlockEvent.HarvestDropsEvent.
	 * <p>
	 * The outer map uses the dimension type as the key and the per-dimension map as the value.
	 * The inner (per-dimension) maps use the broken block's position as the key and its {@link TileEntity} as the value.
	 */
	private static final Map<RegistryKey<DimensionType>, Map<BlockPos, TileEntity>> storedSpawners = new HashMap<>();

	/**
	 * Was the event fired on the server for a mob spawner broken by a player holding a pickaxe?
	 *
	 * @param world  The block's world
	 * @param state  The block state
	 * @param player The player who broke the block
	 * @return Is the event valid?
	 */
	private static boolean isValid(final IWorld world, final BlockState state, @Nullable final PlayerEntity player) {
		if (player == null) return false;

		final ItemStack heldItem = player.getHeldItemMainhand();

		return !world.isRemote() && // Return true if this is the server,
				state.getBlock() == Blocks.SPAWNER && // The block is a mob spawner,
				!heldItem.isEmpty() && heldItem.getHarvestLevel(ToolType.PICKAXE, player, state) > 0 &&  // The held item is a pickaxe,
				EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, heldItem) > 0; // And the held item has Silk Touch
	}

	@SubscribeEvent
	public static void blockBroken(final BlockEvent.BreakEvent event) {
		final IWorld world = event.getWorld();
		final BlockPos pos = event.getPos();

		// If the event isn't valid, do nothing
		if (!isValid(world, event.getState(), event.getPlayer())) return;

		// If the TileEntity isn't a mob spawner, do nothing
		if (!(world.getTileEntity(pos) instanceof MobSpawnerTileEntity)) {
			return;
		}

		// Get this dimension's type
		final DimensionType dimensionType = world.getDimensionType();

		// Get the map for this dimension
//		final Map<BlockPos, TileEntity> storedSpawnersInDimension = storedSpawners.computeIfAbsent(dimensionType, dimension -> new HashMap<>());
//		storedSpawnersInDimension.put(pos, world.getTileEntity(pos)); // Store the TileEntity in it

		event.setExpToDrop(0); // Don't drop any XP
	}

	/*
	@SubscribeEvent
	public static void blockHarvested(final BlockEvent.HarvestDropsEvent event) {
		final IWorld world = event.getWorld();
		final BlockPos pos = event.getPos();
		final BlockState state = event.getState();

		// If the event isn't valid, do nothing
		if (!isValid(world, state, event.getHarvester())) return;

		final DimensionType dimensionType = world.getDimension().getType();

		if (!storedSpawners.containsKey(dimensionType)) { // If the map for this dimension doesn't exist yet, do nothing
			return;
		}

		final Map<BlockPos, TileEntity> storedSpawnersInDimension = storedSpawners.get(dimensionType); // Get the map for this dimension
		final TileEntity tileEntity = storedSpawnersInDimension.remove(pos); // Get the stored TileEntity for this position and remove it from the map
		if (tileEntity == null) return; // If the TileEntity doesn't exist, do nothing

		final CompoundNBT tileData = tileEntity.serializeNBT(); // Write the TileEntity to NBT
		tileData.remove("x"); // Remove the coordinate tags so spawners of the same type from different positions stack
		tileData.remove("y");
		tileData.remove("z");

		final ItemStack droppedItem = new ItemStack(state.getBlock()); // Create an ItemStack of the Block
		droppedItem.setTagInfo("BlockEntityTag", tileData); // Store the TileEntity data in the ItemStack
		event.getDrops().add(droppedItem); // Add the ItemStack to the drops list
	}
	*/

	@SubscribeEvent
	public static void serverStopped(final FMLServerStoppedEvent event) {
		storedSpawners.clear(); // Clear the map when the server stops
	}
}
