package choonster.testmod3.compat.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Gets the {@link ItemStack} from {@link Block#getPickBlock}, but passes the correct {@link IBlockState}.
 * <p>
 * This fixes the model and name displayed for blocks that have a {@link TileEntity} and multiple variants.
 *
 * @author Choonster
 */
public class HUDHandlerVariantTileEntityBlocks implements IWailaDataProvider {
	public static final HUDHandlerVariantTileEntityBlocks INSTANCE = new HUDHandlerVariantTileEntityBlocks();

	/**
	 * Callback used to override the default Waila lookup system.
	 * Will be used if the implementing class is registered via {@link IWailaRegistrar#registerStackProvider}.
	 *
	 * @param accessor Contains most of the relevant information about the current environment.
	 * @param config   Current configuration of Waila.
	 * @return null if override is not required, an ItemStack otherwise.
	 */
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return accessor.getBlock().getPickBlock(accessor.getBlockState(), accessor.getMOP(), accessor.getWorld(), accessor.getPosition(), accessor.getPlayer());
	}

	/**
	 * Callback used to add lines to one of the three sections of the tooltip (Head, Body, Tail).
	 * Will be used if the implementing class is registered via {@link IWailaRegistrar#registerHeadProvider} client side.
	 * You are supposed to always return the modified input currenttip.
	 *
	 * @param itemStack  Current block scanned, in ItemStack form.
	 * @param currenttip Current list of tooltip lines (might have been processed by other providers and might be processed by other providers).
	 * @param accessor   Contains most of the relevant information about the current environment.
	 * @param config     Current configuration of Waila.
	 * @return Modified input currenttip
	 */
	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	/**
	 * Callback used to add lines to one of the three sections of the tooltip (Head, Body, Tail).
	 * Will be used if the implementing class is registered via {@link IWailaRegistrar#registerBodyProvider} client side.
	 * You are supposed to always return the modified input currenttip.
	 *
	 * @param itemStack  Current block scanned, in ItemStack form.
	 * @param currenttip Current list of tooltip lines (might have been processed by other providers and might be processed by other providers).
	 * @param accessor   Contains most of the relevant information about the current environment.
	 * @param config     Current configuration of Waila.
	 * @return Modified input currenttip
	 */
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	/**
	 * Callback used to add lines to one of the three sections of the tooltip (Head, Body, Tail).
	 * Will be used if the implementing class is registered via {@link IWailaRegistrar#registerTailProvider} client side.
	 * You are supposed to always return the modified input currenttip.
	 *
	 * @param itemStack  Current block scanned, in ItemStack form.
	 * @param currenttip Current list of tooltip lines (might have been processed by other providers and might be processed by other providers).
	 * @param accessor   Contains most of the relevant information about the current environment.
	 * @param config     Current configuration of Waila.
	 * @return Modified input currenttip
	 */
	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	/**
	 * Callback used server side to return a custom synchronization NBTTagCompound.
	 * Will be used if the implementing class is registered via {@link IWailaRegistrar#registerNBTProvider} server and client side.
	 * You are supposed to always return the modified input NBTTagCompound tag.
	 *
	 * @param player The player requesting data synchronization (The owner of the current connection).
	 * @param te     The TileEntity targeted for synchronization.
	 * @param tag    Current synchronization tag (might have been processed by other providers and might be processed by other providers).
	 * @param world  TileEntity's World.
	 * @param pos    Position of the TileEntity.
	 * @return Modified input NBTTagCompound tag.
	 */
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
		return tag;
	}
}
