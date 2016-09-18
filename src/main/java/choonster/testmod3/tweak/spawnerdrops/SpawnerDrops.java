package choonster.testmod3.tweak.spawnerdrops;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
@Mod.EventBusSubscriber
public class SpawnerDrops {

	/**
	 * Stores the TileEntities of broken spawner blocks between {@link BlockEvent.BreakEvent} and {@link BlockEvent.HarvestDropsEvent}.
	 * <p>
	 * The outer map uses the dimension ID as the key and the per-dimension map as the value.
	 * The inner (per-dimension) maps use the broken block's position as the key and its {@link TileEntity} as the value.
	 */
	private static final TIntObjectMap<Map<BlockPos, TileEntity>> storedSpawners = new TIntObjectHashMap<>();

	/**
	 * Was the event fired on the server for a mob spawner broken by a player holding a pickaxe?
	 *
	 * @param world  The block's world
	 * @param pos    The block's position
	 * @param state  The block state
	 * @param player The player who broke the block
	 * @return Is the event valid?
	 */
	private static boolean isValid(World world, BlockPos pos, IBlockState state, @Nullable EntityPlayer player) {
		if (player == null) return false;

		final ItemStack heldItem = player.getHeldItemMainhand();

		return !world.isRemote && // Return true if this is the server,
				state.getBlock() == Blocks.MOB_SPAWNER && // The block is a mob spawner,
				heldItem != null && heldItem.getItem().getHarvestLevel(heldItem, "pickaxe", player, state) > 0 &&  // The held item is a pickaxe,
				EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, heldItem) > 0; // And the held item has Silk Touch
	}

	@SubscribeEvent
	public static void blockBroken(BlockEvent.BreakEvent event) {
		final World world = event.getWorld();
		final BlockPos pos = event.getPos();

		// If the event isn't valid, do nothing
		if (!isValid(world, pos, event.getState(), event.getPlayer())) return;

		// If the TileEntity isn't a mob spawner, do nothing
		if (!(world.getTileEntity(pos) instanceof TileEntityMobSpawner)) {
			return;
		}

		final int dimensionID = world.provider.getDimension(); // Get this dimension's ID
		if (!storedSpawners.containsKey(dimensionID)) { // If the map for this dimension doesn't exist yet, create it
			storedSpawners.put(dimensionID, new HashMap<>());
		}

		final Map<BlockPos, TileEntity> storedSpawnersInDimension = storedSpawners.get(dimensionID); // Get the map for this dimension
		storedSpawnersInDimension.put(pos, world.getTileEntity(pos)); // Store the TileEntity in it

		event.setExpToDrop(0); // Don't drop any XP
	}

	@SubscribeEvent
	public static void blockHarvested(BlockEvent.HarvestDropsEvent event) {
		final World world = event.getWorld();
		final BlockPos pos = event.getPos();
		final IBlockState state = event.getState();

		// If the event isn't valid, do nothing
		if (!isValid(world, pos, state, event.getHarvester())) return;

		final int dimensionID = world.provider.getDimension(); // Get this dimension's ID
		if (!storedSpawners.containsKey(dimensionID)) { // If the map for this dimension doesn't exist yet, do nothing
			return;
		}

		final Map<BlockPos, TileEntity> storedSpawnersInDimension = storedSpawners.get(dimensionID); // Get the map for this dimension
		final TileEntity tileEntity = storedSpawnersInDimension.remove(pos); // Get the stored TileEntity for this position and remove it from the map
		if (tileEntity == null) return; // If the TileEntity doesn't exist, do nothing

		final NBTTagCompound tileData = tileEntity.serializeNBT(); // Write the TileEntity to NBT
		tileData.removeTag("x"); // Remove the coordinate tags so spawners of the same type from different positions stack
		tileData.removeTag("y");
		tileData.removeTag("z");

		final ItemStack droppedItem = new ItemStack(state.getBlock()); // Create an ItemStack of the Block
		droppedItem.setTagInfo("BlockEntityTag", tileData); // Store the TileEntity data in the ItemStack
		event.getDrops().add(droppedItem); // Add the ItemStack to the drops list
	}

	public static void serverStopped() {
		storedSpawners.clear(); // Clear the map when the server stops
	}
}
